package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.ingredient;

import java.lang.reflect.Constructor;
import java.util.function.Predicate;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import sun.misc.Unsafe;

public class InjectedIngredient implements Predicate<ItemStack> {

	private static Class<? extends RecipeItemStack> recipeItemStackInjectedClass;
	
	private static Unsafe unsafe;
	
	public static void inject() {
		RecipeItemStackClassLoader loader = new RecipeItemStackClassLoader();
		try {
			recipeItemStackInjectedClass = (Class<? extends RecipeItemStack>) loader
					.defineClass("net.minecraft.server.v1_12_R1.RecipeItemStackInjected", RecipeItemStackInjectedDump.dump());
			
			Constructor<Unsafe> unsafeConstructor = Unsafe.class.getDeclaredConstructor();
			unsafeConstructor.setAccessible(true);
			unsafe = unsafeConstructor.newInstance();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private final Predicate<? super ItemStack> tester;

	public InjectedIngredient(Predicate<? super ItemStack> predicate) {
		this.tester = predicate;
	}

	@Override
	public boolean test(ItemStack itemStack) {
		return tester.test(itemStack);
	}


	public RecipeItemStack asNMSIngredient() {
		try {
			RecipeItemStack recipeItemStackInjected = (RecipeItemStack) unsafe.allocateInstance(recipeItemStackInjectedClass);
			ReflectionUtil.setDeclaredFieldValue(recipeItemStackInjected, "predicate", tester);
			ReflectionUtil.setFinalFieldValue(recipeItemStackInjected, "choices", new ItemStack[0]);
			return recipeItemStackInjected;

			//
			// 		Old implementation: doesn't work due to IllegalAccesError,
			//		even though I define the class in the same package using the same class loader.
			//
			// Constructor<? extends RecipeItemStack> constructor = recipeItemStackInjectedClass.getConstructor(Predicate.class);
			// RecipeItemStack recipeItemStackInjected = constructor.newInstance(tester);
			// return recipeItemstackInjected;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	/*
	 * The class loader to define the RecipeItemStackInjected class
	 */

	private static class RecipeItemStackClassLoader extends ClassLoader {
		public RecipeItemStackClassLoader() {
			super(RecipeItemStack.class.getClassLoader());
		}

		public Class<?> defineClass(String name, byte[] bytecodeBytes) {
			return defineClass(name, bytecodeBytes, 0, bytecodeBytes.length);
		}
	}

	/*
	 * 
	 * 	Java source code of the class:
	 * 
	 * 	package net.minecraft.server.v1_12_R1;
	 * 
	 * 	import java.util.Objects;
	 * 	import java.util.function.Predicate;
	 * 
	 * 	public class RecipeItemStackInjected extends RecipeItemStack implements Predicate<ItemStack> {
	 *
	 * 		private Predicate<? super ItemStack> predicate;
	 *
	 * 		public RecipeItemStackInjected(Predicate<? super ItemStack> injection) {
	 *     		super(new ItemStack[0], null);
	 *     		this.predicate = Objects.requireNonNull(injection);
	 * 		}
	 *
	 *		@Override
	 *		public boolean a(ItemStack itemStack) {
	 * 			return predicate.test(itemStack);
	 *		}
	 *
	 * 		@Override
	 * 		public boolean apply(ItemStack itemStack) {
	 *			return predicate.test(itemStack);
	 *		}
	 *
	 *		@Override
	 *		public void test(ItemStack itemStack) {
	 *			return predicate.test(itemStack) {
	 *		}
	 *
	 *	}
	 *
	 *	THANKS ASM!
	 */

	private static final class RecipeItemStackInjectedDump implements Opcodes {

		public static byte[] dump () throws Exception {

			ClassWriter cw = new ClassWriter(0);
			FieldVisitor fv;
			MethodVisitor mv;

			cw.visit(/*version*/ 52,
					/*access*/ ACC_PUBLIC + ACC_SUPER,
					/*name*/ "net/minecraft/server/v1_12_R1/RecipeItemStackInjected",
					/*signature*/ "Lnet/minecraft/server/v1_12_R1/RecipeItemStack;Ljava/util/function/Predicate<Lnet/minecraft/server/v1_12_R1/ItemStack;>;",
					/*super name*/ "net/minecraft/server/v1_12_R1/RecipeItemStack",
					/*interfaces*/ new String[] { "java/util/function/Predicate" });

			cw.visitSource("RecipeItemStackInjected.java", null);

			{
				fv = cw.visitField(ACC_PRIVATE, "predicate", "Ljava/util/function/Predicate;", "Ljava/util/function/Predicate<-Lnet/minecraft/server/v1_12_R1/ItemStack;>;", null);
				fv.visitEnd();
			}
			{
				mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/util/function/Predicate;)V", "(Ljava/util/function/Predicate<-Lnet/minecraft/server/v1_12_R1/ItemStack;>;)V", null);
				mv.visitCode();				
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(11, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitInsn(ICONST_0);
				mv.visitTypeInsn(ANEWARRAY, "net/minecraft/server/v1_12_R1/ItemStack");
				mv.visitInsn(ACONST_NULL);
				mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/server/v1_12_R1/RecipeItemStack", "<init>", "([Lnet/minecraft/server/v1_12_R1/ItemStack;Lnet/minecraft/server/v1_12_R1/RecipeItemStack$1;)V", false);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(12, l1);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKESTATIC, "java/util/Objects", "requireNonNull", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
				mv.visitTypeInsn(CHECKCAST, "java/util/function/Predicate");
				mv.visitFieldInsn(PUTFIELD, "net/minecraft/server/v1_12_R1/RecipeItemStackInjected", "predicate", "Ljava/util/function/Predicate;");
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLineNumber(13, l2);
				mv.visitInsn(RETURN);
				Label l3 = new Label();
				mv.visitLabel(l3);
				mv.visitLocalVariable("this", "Lnet/minecraft/server/v1_12_R1/RecipeItemStackInjected;", null, l0, l3, 0);
				mv.visitLocalVariable("injection", "Ljava/util/function/Predicate;", "Ljava/util/function/Predicate<-Lnet/minecraft/server/v1_12_R1/ItemStack;>;", l0, l3, 1);
				mv.visitMaxs(3, 2);
				mv.visitEnd();
			}
			{
				mv = cw.visitMethod(ACC_PUBLIC, "a", "(Lnet/minecraft/server/v1_12_R1/ItemStack;)Z", null, null);
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(16, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, "net/minecraft/server/v1_12_R1/RecipeItemStackInjected", "predicate", "Ljava/util/function/Predicate;");
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKEINTERFACE, "java/util/function/Predicate", "test", "(Ljava/lang/Object;)Z", true);
				mv.visitInsn(IRETURN);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLocalVariable("this", "Lnet/minecraft/server/v1_12_R1/RecipeItemStackInjected;", null, l0, l1, 0);
				mv.visitLocalVariable("itemStack", "Lnet/minecraft/server/v1_12_R1/ItemStack;", null, l0, l1, 1);
				mv.visitMaxs(2, 2);
				mv.visitEnd();
			}
			{
				mv = cw.visitMethod(ACC_PUBLIC, "apply", "(Lnet/minecraft/server/v1_12_R1/ItemStack;)Z", null, null);
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(20, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, "net/minecraft/server/v1_12_R1/RecipeItemStackInjected", "predicate", "Ljava/util/function/Predicate;");
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKEINTERFACE, "java/util/function/Predicate", "test", "(Ljava/lang/Object;)Z", true);
				mv.visitInsn(IRETURN);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLocalVariable("this", "Lnet/minecraft/server/v1_12_R1/RecipeItemStackInjected;", null, l0, l1, 0);
				mv.visitLocalVariable("itemStack", "Lnet/minecraft/server/v1_12_R1/ItemStack;", null, l0, l1, 1);
				mv.visitMaxs(2, 2);
				mv.visitEnd();
			}
			{
				mv = cw.visitMethod(ACC_PUBLIC, "test", "(Lnet/minecraft/server/v1_12_R1/ItemStack;)Z", null, null);
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(24, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, "net/minecraft/server/v1_12_R1/RecipeItemStackInjected", "predicate", "Ljava/util/function/Predicate;");
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKEINTERFACE, "java/util/function/Predicate", "test", "(Ljava/lang/Object;)Z", true);
				mv.visitInsn(IRETURN);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLocalVariable("this", "Lnet/minecraft/server/v1_12_R1/RecipeItemStackInjected;", null, l0, l1, 0);
				mv.visitLocalVariable("itemStack", "Lnet/minecraft/server/v1_12_R1/ItemStack;", null, l0, l1, 1);
				mv.visitMaxs(2, 2);
				mv.visitEnd();
			}
			{
				mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "test", "(Ljava/lang/Object;)Z", null, null);
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(1, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitTypeInsn(CHECKCAST, "net/minecraft/server/v1_12_R1/ItemStack");
				mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/server/v1_12_R1/RecipeItemStackInjected", "test", "(Lnet/minecraft/server/v1_12_R1/ItemStack;)Z", false);
				mv.visitInsn(IRETURN);
				mv.visitMaxs(2, 2);
				mv.visitEnd();
			}
			cw.visitEnd();

			return cw.toByteArray();
		}
	}

}
