var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

var targetMethodName = ASMAPI.mapMethod("func_229145_a_");

function initializeCoreMod() {
    return {
        'PlayerRendererTransformer' : {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/client/renderer/entity/PlayerRenderer'
            },
            'transformer':function(cn) {
                cn.methods.forEach(function(mn) {
                    // renderArm
                    if(mn.name.equals(targetMethodName)) {
                        var il = new InsnList();
                        il.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        il.add(new VarInsnNode(Opcodes.ALOAD, 1));
                        il.add(new VarInsnNode(Opcodes.ALOAD, 2));
                        il.add(new VarInsnNode(Opcodes.ILOAD, 3));
                        il.add(new VarInsnNode(Opcodes.ALOAD, 4));
                        il.add(new VarInsnNode(Opcodes.ALOAD, 5));
                        il.add(new VarInsnNode(Opcodes.ALOAD, 6));
                        il.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/craftingdead/core/client/ClientDist", "renderArmsWithExtraSkins", "(Lnet/minecraft/client/renderer/entity/PlayerRenderer;Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/client/entity/player/AbstractClientPlayerEntity;Lnet/minecraft/client/renderer/model/ModelRenderer;Lnet/minecraft/client/renderer/model/ModelRenderer;)V", false));
                        mn.instructions.insert(il);
                    }
                });
                return cn;
            }
        }
    };
}