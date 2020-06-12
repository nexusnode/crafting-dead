var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

function initializeCoreMod() {
    return {
        'MinecraftTransformer' : {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.GameRenderer',
                'methodName': 'updateShaderGroupSize',
                'methodDesc': '(II)V'
            },
            'transformer': function(methodNode) {
                ASMAPI.appendMethodCall(methodNode, new MethodInsnNode(ASMAPI.MethodType.STATIC.toOpcode(), "com/craftingdead/immerse/client/ClientDist", "framebufferResized", "()V", false));
                return methodNode;
            }
        }
    };
}