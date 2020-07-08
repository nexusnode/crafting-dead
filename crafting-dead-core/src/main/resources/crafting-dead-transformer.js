var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

var PlayerRenderer_FQN = 'net.minecraft.client.renderer.entity.PlayerRenderer';
var PlayerRenderer_renderArm_methodName = ASMAPI.mapMethod("func_229145_a_");

var LivingRenderer_FQN = 'net.minecraft.client.renderer.entity.LivingRenderer';
var LivingRenderer_render_methodName = ASMAPI.mapMethod("func_225623_a_");

var LivingRenderer_entityModel_fieldName = ASMAPI.mapField("field_77045_g");
var LivingRenderer_getOverlay_methodName = ASMAPI.mapMethod("func_229117_c_");
var EntityModel_render_methodName = ASMAPI.mapMethod("func_225598_a_");

function initializeCoreMod() {
    return {
        'RenderArmTransformer': {
            'target': {
                'type': 'METHOD',
                'class': PlayerRenderer_FQN,
                'methodName': PlayerRenderer_renderArm_methodName,
                'methodDesc': '(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/client/entity/player/AbstractClientPlayerEntity;Lnet/minecraft/client/renderer/model/ModelRenderer;Lnet/minecraft/client/renderer/model/ModelRenderer;)V'
            },
            'transformer': transformRenderArm
        },
        'RenderTransformer': {
            'target': {
                'type': 'METHOD',
                'class': LivingRenderer_FQN,
                'methodName': LivingRenderer_render_methodName,
                'methodDesc': '(Lnet/minecraft/entity/LivingEntity;FFLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V'
            },
            'transformer': transformRender
        }
    };
}

function transformRenderArm(methodNode) {
    logStart(PlayerRenderer_FQN, PlayerRenderer_renderArm_methodName);
    var instructionList = new InsnList();
    instructionList.add(new VarInsnNode(Opcodes.ALOAD, 0));
    instructionList.add(new VarInsnNode(Opcodes.ALOAD, 1));
    instructionList.add(new VarInsnNode(Opcodes.ALOAD, 2));
    instructionList.add(new VarInsnNode(Opcodes.ILOAD, 3));
    instructionList.add(new VarInsnNode(Opcodes.ALOAD, 4));
    instructionList.add(new VarInsnNode(Opcodes.ALOAD, 5));
    instructionList.add(new VarInsnNode(Opcodes.ALOAD, 6));
    instructionList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/craftingdead/core/client/ClientDist", "renderArmsWithExtraSkins", "(Lnet/minecraft/client/renderer/entity/PlayerRenderer;Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/client/entity/player/AbstractClientPlayerEntity;Lnet/minecraft/client/renderer/model/ModelRenderer;Lnet/minecraft/client/renderer/model/ModelRenderer;)V", false));
    methodNode.instructions.insert(instructionList);
    logFinish(PlayerRenderer_FQN, PlayerRenderer_renderArm_methodName)
    return methodNode;
}

function transformRender(methodNode) {
    logStart(LivingRenderer_FQN, LivingRenderer_render_methodName);
    var startNode = null;
    var endNode = null;
    var instructions = methodNode.instructions.toArray();
    for (var i = 0; i < instructions.length; i++) {
        var instruction = instructions[i];
        if (instruction.getOpcode() == Opcodes.GETFIELD && instruction.name.equals(LivingRenderer_entityModel_fieldName)) {
            var relativeNode = getNthRelativeNode(instruction, -5);
            if (relativeNode != null && relativeNode.getOpcode() == Opcodes.INVOKESTATIC && relativeNode.name.equals(LivingRenderer_getOverlay_methodName)) {
                startNode = instruction.getPrevious();
            }
        } else if (instruction.getOpcode() == Opcodes.INVOKEVIRTUAL && instruction.name.equals(EntityModel_render_methodName) && instruction.getPrevious().getOpcode() == -1) {
            endNode = instruction.getNext();
        }
    }

    if (startNode !== null && endNode !== null) {
        methodNode.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.ALOAD, 0));
        methodNode.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.ALOAD, 1));
        methodNode.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.ALOAD, 4));
        methodNode.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.ALOAD, 5));
        methodNode.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.ILOAD, 6));
        methodNode.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.ILOAD, 19));
        methodNode.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.FLOAD, 14));
        methodNode.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.FLOAD, 13));
        methodNode.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.FLOAD, 12));
        methodNode.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.FLOAD, 10));
        methodNode.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.FLOAD, 11));
        methodNode.instructions.insertBefore(startNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/craftingdead/core/client/ClientDist", "renderLivingPre", "(Lnet/minecraft/client/renderer/entity/LivingRenderer;Lnet/minecraft/entity/LivingEntity;Lcom/mojang/blaze3d/matrix/MatrixStack;Lcom/mojang/blaze3d/vertex/IVertexBuilder;IIFFFFF)Z", false));
        methodNode.instructions.insertBefore(startNode, new JumpInsnNode(Opcodes.IFNE, endNode));

        methodNode.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.ALOAD, 0));
        methodNode.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.ALOAD, 1));
        methodNode.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.ALOAD, 4));
        methodNode.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.ALOAD, 5));
        methodNode.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.ILOAD, 6));
        methodNode.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.ILOAD, 19));
        methodNode.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.FLOAD, 14));
        methodNode.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.FLOAD, 13));
        methodNode.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.FLOAD, 12));
        methodNode.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.FLOAD, 10));
        methodNode.instructions.insertBefore(endNode, new VarInsnNode(Opcodes.FLOAD, 11));
        methodNode.instructions.insertBefore(endNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/craftingdead/core/client/ClientDist", "renderLivingPost", "(Lnet/minecraft/client/renderer/entity/LivingRenderer;Lnet/minecraft/entity/LivingEntity;Lcom/mojang/blaze3d/matrix/MatrixStack;Lcom/mojang/blaze3d/vertex/IVertexBuilder;IIFFFFF)V", false));
        logFinish(LivingRenderer_FQN, LivingRenderer_render_methodName);
    } else {
        logFail(LivingRenderer_FQN, LivingRenderer_render_methodName);
    }
    return methodNode;
}

function getNthRelativeNode(node, n) {
    while (n > 0 && node !== null) {
        node = node.getNext();
        n--;
    }
    while (n < 0 && node !== null) {
        node = node.getPrevious();
        n++;
    }
    return node;
}

function logStart(className, methodName) {
    print("Patching " + className + "#" + methodName);
}

function logFinish(className, methodName) {
    print("Successfully patched " + className + "#" + methodName);
}

function logFail(className, methodName) {
    print("Failed to patch " + className + "#" + methodName);
}