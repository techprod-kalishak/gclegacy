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
        this.textureWidth = 128;
        this.textureHeight = 64;

        this.body = new ModelRenderer(this, 0, 0);
        this.body.addBox(-3F, -3F, -3F, 6, 6, 6, 0);
        this.body.setRotationPoint(0F, 15F, 0F);
        this.body.setTextureSize(128, 64);
        this.body.mirror = true;
        this.setRotation(this.body, 0F, 0F, 0F);
        this.rearEnd = new ModelRenderer(this, 0, 12);
        this.rearEnd.addBox(-5F, -4F, -6F, 10, 8, 12, 0);
        this.rearEnd.setRotationPoint(0F, 15F, 9F);
        this.rearEnd.setTextureSize(128, 64);
        this.rearEnd.mirror = true;
        this.setRotation(this.rearEnd, 0F, 0F, 0F);
        this.leg8 = new ModelRenderer(this, 18, 0);
        this.leg8.addBox(-1F, -1F, -1F, 16, 2, 2, 0);
        this.leg8.setRotationPoint(4F, 15F, -1F);
        this.leg8.setTextureSize(128, 64);
        this.leg8.mirror = true;
        this.setRotation(this.leg8, 0F, 0.5759587F, 0.1919862F);
        this.leg6 = new ModelRenderer(this, 18, 0);
        this.leg6.addBox(-1F, -1F, -1F, 16, 2, 2, 0);
        this.leg6.setRotationPoint(4F, 15F, 0F);
        this.leg6.setTextureSize(128, 64);
        this.leg6.mirror = true;
        this.setRotation(this.leg6, 0F, 0.2792527F, 0.1919862F);
        this.leg4 = new ModelRenderer(this, 18, 0);
        this.leg4.addBox(-1F, -1F, -1F, 16, 2, 2, 0);
        this.leg4.setRotationPoint(4F, 15F, 1F);
        this.leg4.setTextureSize(128, 64);
        this.leg4.mirror = true;
        this.setRotation(this.leg4, 0F, -0.2792527F, 0.1919862F);
        this.leg2 = new ModelRenderer(this, 18, 0);
        this.leg2.addBox(-1F, -1F, -1F, 16, 2, 2, 0);
        this.leg2.setRotationPoint(4F, 15F, 2F);
        this.leg2.setTextureSize(128, 64);
        this.leg2.mirror = true;
        this.setRotation(this.leg2, 0F, -0.5759587F, 0.1919862F);
        this.leg7 = new ModelRenderer(this, 18, 0);
        this.leg7.addBox(-15F, -1F, -1F, 16, 2, 2, 0);
        this.leg7.setRotationPoint(-4F, 15F, -1F);
        this.leg7.setTextureSize(128, 64);
        this.leg7.mirror = true;
        this.setRotation(this.leg7, 0F, -0.5759587F, -0.1919862F);
        this.leg5 = new ModelRenderer(this, 18, 0);
        this.leg5.addBox(-15F, -1F, -1F, 16, 2, 2, 0);
        this.leg5.setRotationPoint(-4F, 15F, 0F);
        this.leg5.setTextureSize(128, 64);
        this.leg5.mirror = true;
        this.setRotation(this.leg5, 0F, -0.2792527F, -0.1919862F);
        this.leg3 = new ModelRenderer(this, 18, 0);
        this.leg3.addBox(-15F, -1F, -1F, 16, 2, 2, 0);
        this.leg3.setRotationPoint(-4F, 15F, 1F);
        this.leg3.setTextureSize(128, 64);
        this.leg3.mirror = true;
        this.setRotation(this.leg3, 0F, 0.2792527F, -0.1919862F);
        this.leg1 = new ModelRenderer(this, 18, 0);
        this.leg1.addBox(-15F, -1F, -1F, 16, 2, 2, 0);
        this.leg1.setRotationPoint(-4F, 15F, 2F);
        this.leg1.setTextureSize(128, 64);
        this.leg1.mirror = true;
        this.setRotation(this.leg1, 0F, 0.5759587F, -0.1919862F);
        this.head = new ModelRenderer(this, 32, 4);
        this.head.addBox(-4F, -4F, -8F, 8, 8, 8, 0);
        this.head.setRotationPoint(0F, 15F, -3F);
        this.head.setTextureSize(128, 64);
        this.head.mirror = true;
        this.setRotation(this.head, 0F, 0F, 0F);

        this.oxygenMask = new ModelRenderer(this, 0, 32);
        this.oxygenMask.addBox(-5F, -5F, -9F, 10, 10, 10, 0);
        this.oxygenMask.setRotationPoint(0F, 15F, -3F);
        this.oxygenMask.setTextureSize(128, 64);
        this.oxygenMask.mirror = true;
        this.setRotation(this.oxygenMask, 0F, 0F, 0F);
        this.tank1 = new ModelRenderer(this, 40, 34);
        this.tank1.addBox(1F, -3F, 0F, 3, 3, 7, 0);
        this.tank1.setRotationPoint(0F, 11F, 4F);
        this.tank1.setTextureSize(128, 64);
        this.tank1.mirror = true;
        this.setRotation(this.tank1, 0F, 0F, 0F);
        this.tank2 = new ModelRenderer(this, 40, 34);
        this.tank2.addBox(-4F, -3F, 0F, 3, 3, 7, 0);
        this.tank2.setRotationPoint(0F, 11F, 4F);
        this.tank2.setTextureSize(128, 64);
        this.tank2.mirror = true;
        this.setRotation(this.tank2, 0F, 0F, 0F);
        this.tube1 = new ModelRenderer(this, 40, 32);
        this.tube1.addBox(2F, 0F, -6.5F, 1, 1, 1, 0);
        this.tube1.setRotationPoint(0F, 11F, 4F);
        this.tube1.setTextureSize(128, 64);
        this.tube1.mirror = true;
        this.setRotation(this.tube1, 0F, 0F, 0F);
        this.tube2 = new ModelRenderer(this, 40, 32);
        this.tube2.addBox(2F, -1F, -5.5F, 1, 1, 1, 0);
        this.tube2.setRotationPoint(0F, 11F, 4F);
        this.tube2.setTextureSize(128, 64);
        this.tube2.mirror = true;
        this.setRotation(this.tube2, 0F, 0F, 0F);
        this.tube3 = new ModelRenderer(this, 40, 32);
        this.tube3.addBox(2F, -1F, -4.5F, 1, 1, 1, 0);
        this.tube3.setRotationPoint(0F, 11F, 4F);
        this.tube3.setTextureSize(128, 64);
        this.tube3.mirror = true;
        this.setRotation(this.tube3, 0F, 0F, 0F);
        this.tube4 = new ModelRenderer(this, 40, 32);
        this.tube4.addBox(2F, -2F, -3.5F, 1, 1, 1, 0);
        this.tube4.setRotationPoint(0F, 11F, 4F);
        this.tube4.setTextureSize(128, 64);
        this.tube4.mirror = true;
        this.setRotation(this.tube4, 0F, 0F, 0F);
        this.tube5 = new ModelRenderer(this, 40, 32);
        this.tube5.addBox(2F, -3F, -2.5F, 1, 1, 1, 0);
        this.tube5.setRotationPoint(0F, 11F, 4F);
        this.tube5.setTextureSize(128, 64);
        this.tube5.mirror = true;
        this.setRotation(this.tube5, 0F, 0F, 0F);
        this.tube6 = new ModelRenderer(this, 40, 32);
        this.tube6.addBox(2F, -4F, -2.5F, 1, 1, 1, 0);
        this.tube6.setRotationPoint(0F, 11F, 4F);
        this.tube6.setTextureSize(128, 64);
        this.tube6.mirror = true;
        this.setRotation(this.tube6, 0F, 0F, 0F);
        this.tube7 = new ModelRenderer(this, 40, 32);
        this.tube7.addBox(2F, -5F, -1.5F, 1, 1, 1, 0);
        this.tube7.setRotationPoint(0F, 11F, 4F);
        this.tube7.setTextureSize(128, 64);
        this.tube7.mirror = true;
        this.setRotation(this.tube7, 0F, 0F, 0F);
        this.tube8 = new ModelRenderer(this, 40, 32);
        this.tube8.addBox(2F, -5F, -0.5F, 1, 1, 1, 0);
        this.tube8.setRotationPoint(0F, 11F, 4F);
        this.tube8.setTextureSize(128, 64);
        this.tube8.mirror = true;
        this.setRotation(this.tube8, 0F, 0F, 0F);
        this.tube9 = new ModelRenderer(this, 40, 32);
        this.tube9.addBox(2F, -4F, 0.5F, 1, 1, 1, 0);
        this.tube9.setRotationPoint(0F, 11F, 4F);
        this.tube9.setTextureSize(128, 64);
        this.tube9.mirror = true;
        this.setRotation(this.tube9, 0F, 0F, 0F);
        this.tube10 = new ModelRenderer(this, 40, 32);
        this.tube10.addBox(-3F, 0F, -6.5F, 1, 1, 1, 0);
        this.tube10.setRotationPoint(0F, 11F, 4F);
        this.tube10.setTextureSize(128, 64);
        this.tube10.mirror = true;
        this.setRotation(this.tube10, 0F, 0F, 0F);
        this.tube11 = new ModelRenderer(this, 40, 32);
        this.tube11.addBox(-3F, -1F, -5.5F, 1, 1, 1, 0);
        this.tube11.setRotationPoint(0F, 11F, 4F);
        this.tube11.setTextureSize(128, 64);
        this.tube11.mirror = true;
        this.setRotation(this.tube11, 0F, 0F, 0F);
        this.tube12 = new ModelRenderer(this, 40, 32);
        this.tube12.addBox(-3F, -1F, -4.5F, 1, 1, 1, 0);
        this.tube12.setRotationPoint(0F, 11F, 4F);
        this.tube12.setTextureSize(128, 64);
        this.tube12.mirror = true;
        this.setRotation(this.tube12, 0F, 0F, 0F);
        this.tube13 = new ModelRenderer(this, 40, 32);
        this.tube13.addBox(-3F, -2F, -3.5F, 1, 1, 1, 0);
        this.tube13.setRotationPoint(0F, 11F, 4F);
        this.tube13.setTextureSize(128, 64);
        this.tube13.mirror = true;
        this.setRotation(this.tube13, 0F, 0F, 0F);
        this.tube15 = new ModelRenderer(this, 40, 32);
        this.tube15.addBox(-3F, -4F, -2.5F, 1, 1, 1, 0);
        this.tube15.setRotationPoint(0F, 11F, 4F);
        this.tube15.setTextureSize(128, 64);
        this.tube15.mirror = true;
        this.setRotation(this.tube15, 0F, 0F, 0F);
        this.tube14 = new ModelRenderer(this, 40, 32);
        this.tube14.addBox(-3F, -3F, -2.5F, 1, 1, 1, 0);
        this.tube14.setRotationPoint(0F, 11F, 4F);
        this.tube14.setTextureSize(128, 64);
        this.tube14.mirror = true;
        this.setRotation(this.tube14, 0F, 0F, 0F);
        this.tube16 = new ModelRenderer(this, 40, 32);
        this.tube16.addBox(-3F, -5F, -1.5F, 1, 1, 1, 0);
        this.tube16.setRotationPoint(0F, 11F, 4F);
        this.tube16.setTextureSize(128, 64);
        this.tube16.mirror = true;
        this.setRotation(this.tube16, 0F, 0F, 0F);
        this.tube17 = new ModelRenderer(this, 40, 32);
        this.tube17.addBox(-3F, -5F, -0.5F, 1, 1, 1, 0);
        this.tube17.setRotationPoint(0F, 11F, 4F);
        this.tube17.setTextureSize(128, 64);
        this.tube17.mirror = true;
        this.setRotation(this.tube17, 0F, 0F, 0F);
        this.tube18 = new ModelRenderer(this, 40, 32);
        this.tube18.addBox(-3F, -4F, 0.5F, 1, 1, 1, 0);
        this.tube18.setRotationPoint(0F, 11F, 4F);
        this.tube18.setTextureSize(128, 64);
        this.tube18.mirror = true;
        this.setRotation(this.tube18, 0F, 0F, 0F);
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