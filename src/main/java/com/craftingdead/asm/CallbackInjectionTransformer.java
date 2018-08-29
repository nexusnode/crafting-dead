package com.craftingdead.asm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.craftingdead.mod.common.core.CraftingDead;

import net.minecraft.launchwrapper.IClassTransformer;

public abstract class CallbackInjectionTransformer implements IClassTransformer {

    private Map<String, Map<String, Callback>> callbackMappings = new HashMap<String, Map<String, Callback>>();

    public CallbackInjectionTransformer() {
        addCallbacks();
    }

    protected abstract void addCallbacks();

    protected final void addCallback(String className, String methodName, String methodSignature, Callback callback) {
        if (!this.callbackMappings.containsKey(className)) {
            this.callbackMappings.put(className, new HashMap<String, Callback>());
        }
        this.callbackMappings.get(className).put(methodName + methodSignature, callback);
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (this.callbackMappings.containsKey(transformedName)) {
            Map<String, Callback> mappings = this.callbackMappings.get(transformedName);

            ClassReader classReader = new ClassReader(basicClass);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, ClassReader.EXPAND_FRAMES);

            for (Iterator<MethodNode> localIterator = classNode.methods.iterator(); localIterator.hasNext(); ) {
                MethodNode method = (MethodNode) localIterator.next();
                String methodSignature = method.name + method.desc;

                if (mappings != null && mappings.containsKey(methodSignature)) {
                    Callback callback = (Callback) mappings.get(methodSignature);
                    if (callback != null) {

                        InstructionBuilder builder = new InstructionBuilder(classNode, method);
                        List<InstructionBuilder.MethodCallFlag> callFlags = new ArrayList<InstructionBuilder.MethodCallFlag>();

                        switch (callback.getType()) {
                            case REDIRECT:
                                callFlags.add(InstructionBuilder.MethodCallFlag.ADD_INSTANCE_ARGUMENT);
                                callFlags.add(InstructionBuilder.MethodCallFlag.FORWARD_EXISTING_ARGUMENTS);
                                callFlags.add(InstructionBuilder.MethodCallFlag.RETURN);

                                builder.addStaticMethodCall(callback.getCallbackClass(), callback.getCallbackMethod(),
                                        callFlags.toArray(new InstructionBuilder.MethodCallFlag[0]));

                                CraftingDead.LOGGER.info("Injecting {} callback for {} in class {}",
                                        callback.getType().name().toLowerCase(), callback, transformedName);
                                method.instructions.insert(builder.build());
                                break;
                            case EVENT:
                                callFlags.add(InstructionBuilder.MethodCallFlag.ADD_INSTANCE_ARGUMENT);
                                callFlags.add(InstructionBuilder.MethodCallFlag.FORWARD_EXISTING_ARGUMENTS);

                                builder.addStaticMethodCall(callback.getCallbackClass(), callback.getCallbackMethod(),
                                        callFlags.toArray(new InstructionBuilder.MethodCallFlag[0]));

                                CraftingDead.LOGGER.info("Injecting {} callback for {} in class {}",
                                        callback.getType().name().toLowerCase(), callback, transformedName);
                                method.instructions.insert(builder.build());
                                break;
                        }

                    }
                }

            }
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            classNode.accept(writer);
            return writer.toByteArray();
        }
        return basicClass;
    }

}
