package com.gmail.jannyboy11.customrecipes.util;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

public class MapIterator<T, R> implements Iterator<R> {
	
	private final Iterator<? extends T> source;
	private final Function<? super T, ? extends R> mapper;
	
	public MapIterator(Iterator<? extends T> source, Function<? super T, ? extends R> mapper) {
		this.source = Objects.requireNonNull(source);
		this.mapper = Objects.requireNonNull(mapper);
	}

	@Override
	public boolean hasNext() {
		return source.hasNext();
	}

	@Override
	public R next() {
		return mapper.apply(source.next());
	}
	
	@Override
	public void remove() {
		source.remove();
	}

}
