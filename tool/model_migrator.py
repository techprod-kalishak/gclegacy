import re
import sys

def convert_model_renderer_to_part_definition(java_code: str) -> str:
    parts = {}

    init_pattern = re.compile(r'this\.(\w+)\s*=\s*new\s+ModelRenderer\s*\(\s*this\s*,\s*(\d+)\s*,\s*(\d+)\s*\);')
    box_pattern = re.compile(r'this\.(\w+)\.addBox\s*\(\s*([^)]+)\s*\);')
    pos_pattern = re.compile(r'this\.(\w+)\.setRotationPoint\s*\(\s*([^)]+)\s*\);')
    mirror_pattern = re.compile(r'this\.(\w+)\.mirror\s*=\s*true;')
    rot_pattern = re.compile(r'(?:this\.)?setRotation\s*\(\s*this\.(\w+)\s*,\s*([^)]+)\s*\);')

    for line in java_code.splitlines():
        line = line.strip()
        if not line:
            continue

        init_match = init_pattern.search(line)
        if init_match:
            name, tex_x, tex_y = init_match.groups()
            if name not in parts:
                parts[name] = {"mirror": False, "rot": "0.0F, 0.0F, 0.0F"}
            parts[name]["tex"] = f"{tex_x}, {tex_y}"
            continue

        box_match = box_pattern.search(line)
        if box_match:
            name, args = box_match.groups()
            clean_args = ", ".join([f"{float(x.strip().replace('F','')): .1f}F".replace('.0F', '.0F') if 'F' in x or '.' in x else f"{float(x.strip()):.1f}F" for x in args.split(',')])
            parts[name]["box"] = clean_args
            continue

        pos_match = pos_pattern.search(line)
        if pos_match:
            name, args = pos_match.groups()
            clean_args = ", ".join([x.strip() if 'F' in x or '.' in x else f"{x.strip()}.0F" for x in args.split(',')])
            parts[name]["pos"] = clean_args
            continue

        mirror_match = mirror_pattern.search(line)
        if mirror_match:
            name = mirror_match.group(1)
            parts[name]["mirror"] = True
            continue

        rot_match = rot_pattern.search(line)
        if rot_match:
            name, args = rot_match.groups()
            clean_rot = []
            for arg in args.split(','):
                arg = arg.strip()
                if "this.toRadians" in arg:
                    # Translate legacy helper methods to clean vanilla Mth math classes
                    deg = arg.replace("this.toRadians(", "").replace(")", "").replace("F", "")
                    clean_rot.append(f"{float(deg):.1f}F * Mth.DEG_TO_RAD")
                else:
                    clean_rot.append(arg)
            parts[name]["rot"] = ", ".join(clean_rot)

    output_lines = [
        "// Auto-generated modern PartDefinitions using Python converter",
        "MeshDefinition meshdefinition = new MeshDefinition();",
        "PartDefinition partdefinition = meshdefinition.getRoot();\n"
    ]

    for name, data in parts.items():
        mirror_str = ".mirror()" if data.get("mirror") else ""
        tex_str = data.get("tex", "0, 0")
        box_str = data.get("box", "0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F")
        pos_str = data.get("pos", "0.0F, 0.0F, 0.0F")
        rot_str = data.get("rot", "0.0F, 0.0F, 0.0F")

        modern_snippet = (
            f'partdefinition.addOrReplaceChild("{name}",\n'
            f'    CubeListBuilder.create(){mirror_str}.texOffs({tex_str})\n'
            f'        .addBox({box_str}),\n'
            f'    PartPose.offsetAndRotation({pos_str}, {rot_str})\n'
            f');\n'
        )
        output_lines.append(modern_snippet)

    return "\n".join(output_lines)

def example():
    legacy_java_input = """
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.villagerHead = new ModelRenderer(this)
        this.villagerHead.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
        this.villagerHead.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, par1 + 0.001F);
        this.villagerNose = new ModelRenderer(this)
        this.villagerNose.setRotationPoint(0.0F, par2 - 2.0F, 0.0F);
        this.villagerNose.setTextureOffset(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, par1 + 0.002F);
        this.villagerHead.addChild(this.villagerNose);
        this.villagerBody = new ModelRenderer(this)
        this.villagerBody.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
        this.villagerBody.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, par1 + 0.003F);
        this.villagerBody.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, par1 + 0.5F + 0.004F);
        this.villagerArms = new ModelRenderer(this)
        this.villagerArms.setRotationPoint(0.0F, 0.0F + par2 + 2.0F, 0.0F);
        this.villagerArms.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4, 8, 4, par1 + 0.005F);
        this.villagerArms.setTextureOffset(44, 22).addBox(4.0F, -2.0F, -2.0F, 4, 8, 4, par1 + 0.0001F);
        this.villagerArms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8, 4, 4, par1 + 0.0004F);
        this.rightVillagerLeg = new ModelRenderer(this, 0, 22)
        this.rightVillagerLeg.setRotationPoint(-2.0F, 12.0F + par2, 0.0F);
        this.rightVillagerLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, par1 + 0.0006F);
        this.leftVillagerLeg = new ModelRenderer(this, 0, 22)
        this.leftVillagerLeg.mirror = true;
        this.leftVillagerLeg.setRotationPoint(2.0F, 12.0F + par2, 0.0F);
        this.leftVillagerLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, par1 + 0.0002F);
        this.brain = new ModelRenderer(this)
        this.brain.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
        this.brain.setTextureOffset(32, 0).addBox(-4.0F, -16.0F, -4.0F, 8, 8, 8, par1 + 0.5F);
    """

    converted_output = convert_model_renderer_to_part_definition(legacy_java_input)
    print(converted_output)

def main():
    args = sys.argv

    if len(args) >= 2 and args[1] == '--file':
        legacy_input = ""

        with open("input.txt", 'r') as fin:
            legacy_input = fin.read()

        with open("output.txt", 'w') as fout:
            fout.write(convert_model_renderer_to_part_definition(legacy_input))
            fout.close()
    else:
        example()

if __name__ == "__main__":
    main()