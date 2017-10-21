package com.gmail.jannyboy11.customrecipes.api.crafting.modify;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.function.Function;

public class ModifiedInvocationHandler<T, R> implements InvocationHandler {
    
    private final T base;
    private final Function<? super T, ? extends R> modifier;
    private final R result;
    
    public ModifiedInvocationHandler(T base, Function<? super T, ? extends R> modifier) {
        this.base = base;
        this.modifier = modifier;
        this.result = modifier.apply(base);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
        //proxy implements ModifiedThing
        
        final String methodName = method.getName();
        
        switch(methodName) {
            case "getBase": return base;
            case "getModifier": return modifier;
        }
        
        final Class<?>[] parameterTypes = method.getParameterTypes();
        
        try {
            Method resultMethod = result.getClass().getMethod(methodName, parameterTypes);
            return resultMethod.invoke(result, arguments);
        } catch (NoSuchMethodException e) {
            Method resultMethod = base.getClass().getMethod(methodName, parameterTypes);
            return resultMethod.invoke(base, arguments);
        }
    }

}
