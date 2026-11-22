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
        
        this.keyParts4 = new ModelRenderer(this, 50, 43);
        this.keyParts4.addBox(7F, 2F, -0.5F, 3, 1, 1);
        this.keyParts4.setRotationPoint(0F, 0F, 0F);
        this.keyParts4.setTextureSize(64, 64);
        this.keyParts4.mirror = true;
        this.keyParts3 = new ModelRenderer(this, 39, 43);
        this.keyParts3.addBox(6F, 1F, -0.5F, 4, 1, 1);
        this.keyParts3.setRotationPoint(0F, 0F, 0F);
        this.keyParts3.setTextureSize(64, 64);
        this.keyParts3.mirror = true;
        this.keyParts2 = new ModelRenderer(this, 14, 43);
        this.keyParts2.addBox(-0.5F, 0F, -0.5F, 11, 1, 1);
        this.keyParts2.setRotationPoint(0F, 0F, 0F);
        this.keyParts2.setTextureSize(64, 64);
        this.keyParts2.mirror = true;
        this.keyParts1 = new ModelRenderer(this, 9, 43);
        this.keyParts1.addBox(-1.5F, -0.5F, -0.5F, 1, 2, 1);
        this.keyParts1.setRotationPoint(0F, 0F, 0F);
        this.keyParts1.setTextureSize(64, 64);
        this.keyParts1.mirror = true;
        this.keyParts0 = new ModelRenderer(this, 0, 43);
        this.keyParts0.addBox(-4.5F, -1F, -0.5F, 3, 3, 1);
        this.keyParts0.setRotationPoint(0F, 0F, 0F);
        this.keyParts0.setTextureSize(64, 64);
        this.keyParts0.mirror = true;
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