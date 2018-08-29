package com.craftingdead.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class InstructionBuilder {

    private InsnList instructions = new InsnList();

    private ClassNode ownerClass;

    private MethodNode methodToModify;

    private boolean isStatic;

    private Type returnType;

    public InstructionBuilder(ClassNode ownerClass, MethodNode methodToModify) {
        this.ownerClass = ownerClass;
        this.methodToModify = methodToModify;
        this.returnType = Type.getReturnType(methodToModify.desc);
        this.isStatic = (methodToModify.access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC;
    }

    public void addStaticMethodCall(String ownerClass, String methodName, MethodCallFlag... callFlags) {
        boolean returnValue = false;

        int argVarCount = 0;
        StringBuilder methodCallArgs = new StringBuilder();

        for (MethodCallFlag flag : callFlags) {
            switch (flag) {
                case ADD_INSTANCE_ARGUMENT:
                    if (!isStatic) {
                        instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        argVarCount += 1;
                        methodCallArgs.append(Type.getObjectType(this.ownerClass.name));
                    }
                    break;
                case FORWARD_EXISTING_ARGUMENTS:
                    for (Type type : Type.getArgumentTypes(methodToModify.desc)) {
                        instructions.add(new VarInsnNode(type.getOpcode(Opcodes.ILOAD), argVarCount));
                        argVarCount += type.getSize();
                        methodCallArgs.append(type.toString());
                    }
                    break;
                case RETURN:
                    returnValue = true;
                    break;
            }

        }

        String methodDescription = String.format("(%s)%s",
                new Object[]{methodCallArgs.toString(), returnValue ? returnType : Type.VOID_TYPE});

        instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ownerClass, methodName, methodDescription, false));

        if (returnValue) {
            instructions.add(new InsnNode(returnType.getOpcode(Opcodes.IRETURN)));
        }
    }

    public InsnList build() {
        return this.instructions;
    }

    public static enum MethodCallFlag {
        /**
         * Adds an instance of the owner class as the first argument, if the method is
         * static this flag will be ignored.
         */
        ADD_INSTANCE_ARGUMENT,
        /**
         * Forwards any existing arguments
         */
        FORWARD_EXISTING_ARGUMENTS,
        /**
         * Returns the value that is returned by the method call, this has to match the existing method return type
         */
        RETURN;
    }

}
