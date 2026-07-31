import re

def convert_model_renderer_to_part_definition(java_code: str) -> str:
    # Dictionary to collect properties of each part during parsing
    parts = {}

    # Regular expression patterns to extract legacy method arguments
    init_pattern = re.compile(r'this\.(\w+)\s*=\s*new\s+ModelRenderer\s*\(\s*this\s*,\s*(\d+)\s*,\s*(\d+)\s*\);')
    box_pattern = re.compile(r'this\.(\w+)\.addBox\s*\(\s*([^)]+)\s*\);')
    pos_pattern = re.compile(r'this\.(\w+)\.setRotationPoint\s*\(\s*([^)]+)\s*\);')
    mirror_pattern = re.compile(r'this\.(\w+)\.mirror\s*=\s*true;')

    # Matches both this.setRotation(part, x, y, z) and standard rotation assignments
    rot_pattern = re.compile(r'(?:this\.)?setRotation\s*\(\s*this\.(\w+)\s*,\s*([^)]+)\s*\);')

    # Step 1: Parse the legacy Java source text line by line
    for line in java_code.splitlines():
        line = line.strip()
        if not line:
            continue

        # Find initial instantiation & texture offsets
        init_match = init_pattern.search(line)
        if init_match:
            name, tex_x, tex_y = init_match.groups()
            if name not in parts:
                parts[name] = {"mirror": False, "rot": "0.0F, 0.0F, 0.0F"}
            parts[name]["tex"] = f"{tex_x}, {tex_y}"
            continue

        # Find box dimensions
        box_match = box_pattern.search(line)
        if box_match:
            name, args = box_match.groups()
            # Standardize float literals to include decimals for clean modern Java code
            clean_args = ", ".join([f"{float(x.strip().replace('F','')): .1f}F".replace('.0F', '.0F') if 'F' in x or '.' in x else f"{float(x.strip()):.1f}F" for x in args.split(',')])
            parts[name]["box"] = clean_args
            continue

        # Find pivot positions
        pos_match = pos_pattern.search(line)
        if pos_match:
            name, args = pos_match.groups()
            clean_args = ", ".join([x.strip() if 'F' in x or '.' in x else f"{x.strip()}.0F" for x in args.split(',')])
            parts[name]["pos"] = clean_args
            continue

        # Find mirroring flags
        mirror_match = mirror_pattern.search(line)
        if mirror_match:
            name = mirror_match.group(1)
            parts[name]["mirror"] = True
            continue

        # Find initial rotation properties
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

    # Step 2: Generate modern ModelPart output mappings
    output_lines = [
        "// Auto-generated modern PartDefinitions using Python converter",
        "MeshDefinition meshdefinition = new MeshDefinition();",
        "PartDefinition partdefinition = meshdefinition.getRoot();\n"
    ]

    for name, data in parts.items():
        # Handle mirroring rules safely inside Modern CubeListBuilders
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


# --- EXAMPLE RUN ---
if __name__ == "__main__":
    # Paste your source block inside the multi-line string variable
    legacy_java_input = """
                        this.base1a = new ModelRenderer(this, 113, 24);
                this.base1a.addBox(-2.5F, -2F, -7.5F, 5, 2, 15);
                this.base1a.setRotationPoint(0F, 24F, 0F);
                this.base1a.setTextureSize(256, 128);
                this.base1a.mirror = true;
                this.setRotation(this.base1a, 0F, halfPI, 0F);
                this.baseAux1 = new ModelRenderer(this, 35, 40);
                this.baseAux1.addBox(-3.5F, 0F, -3.5F, 7, 1, 7);
                this.baseAux1.setRotationPoint(0F, 20F, 0F);
                this.baseAux1.setTextureSize(256, 128);
                this.baseAux1.mirror = true;
                this.setRotation(this.baseAux1, 0F, 0.7853982F, 0F);
                this.base1b = new ModelRenderer(this, 113, 24);
                this.base1b.addBox(-2.5F, -2F, -7.5F, 5, 2, 15);
                this.base1b.setRotationPoint(0F, 24F, 0F);
                this.base1b.setTextureSize(256, 128);
                this.base1b.mirror = true;
                this.setRotation(this.base1b, 0F, 0F, 0F);
                this.base2a = new ModelRenderer(this, 65, 0);
                this.base2a.addBox(-3.5F, -2F, -7F, 7, 5, 14);
                this.base2a.setRotationPoint(0F, 23F, 0F);
                this.base2a.setTextureSize(256, 128);
                this.base2a.mirror = true;
                this.setRotation(this.base2a, 0F, -0.7853982F, 0F);
                this.base2b = new ModelRenderer(this, 65, 0);
                this.base2b.addBox(-3.5F, -2F, -7F, 7, 3, 14);
                this.base2b.setRotationPoint(0F, 23F, 0F);
                this.base2b.setTextureSize(256, 128);
                this.base2b.mirror = true;
                this.setRotation(this.base2b, 0F, 0.7853982F, 0F);
                this.baseAux3 = new ModelRenderer(this, 50, 62);
                this.baseAux3.addBox(-8.5F, 0F, -0.5F, 17, 4, 1);
                this.baseAux3.setRotationPoint(0F, 20.5F, 0F);
                this.baseAux3.setTextureSize(256, 128);
                this.baseAux3.mirror = true;
                this.setRotation(this.baseAux3, 0F, -0.7853982F, 0F);
                this.baseAux2 = new ModelRenderer(this, 50, 62);
                this.baseAux2.addBox(-8.5F, 0F, -0.5F, 17, 4, 1);
                this.baseAux2.setRotationPoint(0F, 20.5F, 0F);
                this.baseAux2.setTextureSize(256, 128);
                this.baseAux2.mirror = true;
                this.setRotation(this.baseAux2, 0F, 0.7853982F, 0F);
                this.clawL1 = new ModelRenderer(this, 7, 57);
                this.clawL1.addBox(-0.5F, -1F, -3F, 1, 2, 3);
                this.clawL1.setRotationPoint(-1F, -0.2F, 4F);
                this.clawL1.setTextureSize(256, 128);
                this.clawL1.mirror = true;
                this.setRotation(this.clawL1, 0F, 1.003826F, 0F);
                this.clawL2 = new ModelRenderer(this, 7, 57);
                this.clawL2.addBox(-2.7F, -1F, -5F, 1, 2, 3);
                this.clawL2.setRotationPoint(-1F, -0.2F, 4F);
                this.clawL2.setTextureSize(256, 128);
                this.clawL2.mirror = true;
                this.setRotation(this.clawL2, 0F, 0.1698892F, 0F);
                this.clawR1 = new ModelRenderer(this, 7, 57);
                this.clawR1.addBox(-0.5F, -1F, -3F, 1, 2, 3);
                this.clawR1.setRotationPoint(1F, -0.2F, 4F);
                this.clawR1.setTextureSize(256, 128);
                this.clawR1.mirror = true;
                this.setRotation(this.clawR1, 0F, -1.041005F, 0F);
                this.clawR2 = new ModelRenderer(this, 7, 57);
                this.clawR2.addBox(1.7F, -1F, -5F, 1, 2, 3);
                this.clawR2.setRotationPoint(1F, -0.2F, 4F);
                this.clawR2.setTextureSize(256, 128);
                this.clawR2.mirror = true;
                this.setRotation(this.clawR2, 0F, -0.1896157F, 0F);
                this.clawRPR = new ModelRenderer(this, 0, 45);
                this.clawRPR.addBox(-2.2F, -1.5F, -3F, 1, 3, 1);
                this.clawRPR.setRotationPoint(1F, -0.2F, 4F);
                this.clawRPR.setTextureSize(256, 128);
                this.clawRPR.mirror = true;
                this.setRotation(this.clawRPR, 0F, -1.63514F, 0F);
                this.clawRPL = new ModelRenderer(this, 0, 45);
                this.clawRPL.addBox(-2.2F, -1.5F, -3F, 1, 3, 1);
                this.clawRPL.setRotationPoint(-1F, -0.2F, 4F);
                this.clawRPL.setTextureSize(256, 128);
                this.clawRPL.mirror = true;
                this.setRotation(this.clawRPL, 0F, 0.4096913F, 0F);
                this.armB1 = new ModelRenderer(this, 46, 0);
                this.armB1.addBox(-1F, -12F, -1F, 2, 12, 2);
                this.armB1.setRotationPoint(0F, 22F, 8F);
                this.armB1.setTextureSize(256, 128);
                this.armB1.mirror = true;
                this.setRotation(this.armB1, -0.3005282F, 0F, 0F);
                this.manipulatorRotationPointB = new ModelRenderer(this, 0, 69);
                this.manipulatorRotationPointB.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
                this.manipulatorRotationPointB.setRotationPoint(0F, 0F, 4.5F);
                this.manipulatorRotationPointB.setTextureSize(256, 128);
                this.manipulatorRotationPointB.mirror = true;
                this.setRotation(this.manipulatorRotationPointB, 0F, halfPI, 0F);
                this.baseRotationPointB = new ModelRenderer(this, 0, 69);
                this.baseRotationPointB.addBox(-1.5F, -1.2F, -1.5F, 3, 3, 3);
                this.baseRotationPointB.setRotationPoint(0F, 22F, 7.5F);
                this.baseRotationPointB.setTextureSize(256, 128);
                this.baseRotationPointB.mirror = true;
                this.setRotation(this.baseRotationPointB, this.toRadians(0), this.toRadians(90), this.toRadians(15));
                this.armRotationPointB = new ModelRenderer(this, 0, 69);
                this.armRotationPointB.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
                this.armRotationPointB.setRotationPoint(0F, 10F, 11.5F);
                this.armRotationPointB.setTextureSize(256, 128);
                this.armRotationPointB.mirror = true;
                this.setRotation(this.armRotationPointB, this.toRadians(90), this.toRadians(0), this.toRadians(-90));
                this.armB2 = new ModelRenderer(this, 46, 0);
                this.armB2.addBox(-1F, -12F, -1F, 2, 12, 2);
                this.armB2.setRotationPoint(0F, 9F, 11F);
                this.armB2.setTextureSize(256, 128);
                this.armB2.mirror = true;
                this.setRotation(this.armB2, 0.6289468F, 0F, 0F);
                this.armR1 = new ModelRenderer(this, 46, 0);
                this.armR1.addBox(-1F, -12F, -1F, 2, 12, 2);
                this.armR1.setRotationPoint(-8F, 22F, 0F);
                this.armR1.setTextureSize(256, 128);
                this.armR1.mirror = true;
                this.setRotation(this.armR1, 0.6351428F, halfPI, 0F);
                this.armR2 = new ModelRenderer(this, 55, 0);
                this.armR2.addBox(-1F, -8F, -1F, 2, 8, 2);
                this.armR2.setRotationPoint(-15F, 11F, 0F);
                this.armR2.setTextureSize(256, 128);
                this.armR2.mirror = true;
                this.setRotation(this.armR2, -0.9635439F, halfPI, 0F);
                this.baseRotationPointR = new ModelRenderer(this, 0, 69);
                this.baseRotationPointR.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
                this.baseRotationPointR.setRotationPoint(-7.9F, 22F, 0F);
                this.baseRotationPointR.setTextureSize(256, 128);
                this.baseRotationPointR.mirror = true;
                this.setRotation(this.baseRotationPointR, 0F, 0F, 0.5235988F);
                this.armRotationPointR = new ModelRenderer(this, 0, 69);
                this.armRotationPointR.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
                this.armRotationPointR.setRotationPoint(-15.76667F, 11.5F, 0F);
                this.armRotationPointR.setTextureSize(256, 128);
                this.armRotationPointR.mirror = true;
                this.setRotation(this.armRotationPointR, 0F, 0F, -0.1745329F);
                this.weldHead = new ModelRenderer(this, 17, 0);
                this.weldHead.addBox(-2F, -1.5F, -1.5F, 5, 3, 3);
                this.weldHead.setRotationPoint(-9F, 6F, 0F);
                this.weldHead.setTextureSize(256, 128);
                this.weldHead.mirror = true;
                this.setRotation(this.weldHead, 0F, 0F, 0.5948578F);
                this.weldBit = new ModelRenderer(this, 0, 0);
                this.weldBit.addBox(0F, -0.5F, -0.5F, 7, 1, 1);
                this.weldBit.setRotationPoint(-9F, 6F, 0F);
                this.weldBit.setTextureSize(256, 128);
                this.weldBit.mirror = true;
                this.setRotation(this.weldBit, 0F, 0F, 0.5948606F);
                this.screenRotationPointF = new ModelRenderer(this, 0, 77);
                this.screenRotationPointF.addBox(-1.5F, -1.5F, -1.5F, 3, 2, 3);
                this.screenRotationPointF.setRotationPoint(0F, 14F, -9F);
                this.screenRotationPointF.setTextureSize(256, 128);
                this.screenRotationPointF.mirror = true;
                this.setRotation(this.screenRotationPointF, this.toRadians(30), this.toRadians(0), this.toRadians(0));
                this.armF1 = new ModelRenderer(this, 55, 0);
                this.armF1.addBox(-1F, -8F, -1F, 2, 8, 2);
                this.armF1.setRotationPoint(0F, 21F, -7.5F);
                this.armF1.setTextureSize(256, 128);
                this.armF1.mirror = true;
                this.setRotation(this.armF1, 0.2602503F, 0F, 0F);
                this.baseRotationPointF = new ModelRenderer(this, 0, 69);
                this.baseRotationPointF.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
                this.baseRotationPointF.setRotationPoint(0F, 21.6F, -6.8F);
                this.baseRotationPointF.setTextureSize(256, 128);
                this.baseRotationPointF.mirror = true;
                this.setRotation(this.baseRotationPointF, this.toRadians(30F), this.toRadians(0), this.toRadians(0));
                this.screen = new ModelRenderer(this, 10, 45);
                this.screen.addBox(-3.5F, -2.5F, -1F, 7, 5, 1);
                this.screen.setRotationPoint(0F, 13F, -10F);
                this.screen.setTextureSize(256, 128);
                this.screen.mirror = true;
                this.setRotation(this.screen, -1.047198F, 0F, 0F);
                this.armRotationPointL = new ModelRenderer(this, 0, 69);
                this.armRotationPointL.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
                this.armRotationPointL.setRotationPoint(13.8F, 18.4F, 0F);
                this.armRotationPointL.setTextureSize(256, 128);
                this.armRotationPointL.mirror = true;
                this.setRotation(this.armRotationPointL, 0F, -3.141593F, 0.5235988F);
                this.armL2 = new ModelRenderer(this, 55, 0);
                this.armL2.addBox(-1F, -8F, -1F, 2, 8, 2);
                this.armL2.setRotationPoint(13.7F, 18F, 0F);
                this.armL2.setTextureSize(256, 128);
                this.armL2.mirror = true;
                this.setRotation(this.armL2, 0.6351428F, halfPI, 0F);
                this.baseRotationPointL = new ModelRenderer(this, 0, 69);
                this.baseRotationPointL.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
                this.baseRotationPointL.setRotationPoint(7.5F, 22F, 0F);
                this.baseRotationPointL.setTextureSize(256, 128);
                this.baseRotationPointL.mirror = true;
                this.setRotation(this.baseRotationPointL, 0F, -3.141593F, 0.5235988F);
                this.armL1 = new ModelRenderer(this, 55, 0);
                this.armL1.addBox(-1F, -8F, -1F, 2, 8, 2);
                this.armL1.setRotationPoint(8F, 22F, 0F);
                this.armL1.setTextureSize(256, 128);
                this.armL1.mirror = true;
                this.setRotation(this.armL1, -1.037895F, halfPI, 0F);
                this.sensorDish = new ModelRenderer(this, 68, 41);
                this.sensorDish.addBox(-1F, -2F, -2F, 1, 4, 4);
                this.sensorDish.setRotationPoint(6F, 12F, 0F);
                this.sensorDish.setTextureSize(256, 128);
                this.sensorDish.mirror = true;
                this.setRotation(this.sensorDish, 0F, 0F, -0.3005282F);
                this.sensor = new ModelRenderer(this, 60, 54);
                this.sensor.addBox(-3F, -2F, -1F, 5, 2, 2);
                this.sensor.setRotationPoint(9F, 12F, 0F);
                this.sensor.setTextureSize(256, 128);
                this.sensor.mirror = true;
                this.setRotation(this.sensor, 0F, 0F, -0.3005353F);
    """

    converted_output = convert_model_renderer_to_part_definition(legacy_java_input)
    print(converted_output)