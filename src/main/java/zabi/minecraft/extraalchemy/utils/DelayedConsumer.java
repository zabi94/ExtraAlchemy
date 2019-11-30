package zabi.minecraft.extraalchemy.utils;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.Lists;

public abstract class DelayedConsumer<T> {
	
	private List<Consumer<T>> tasks = Lists.newArrayList();
	private T key = null;

	public void consumeWhenReady(Consumer<T> task) {
		if (key == null) {
			tasks.add(task);
		} else {
			task.accept(key);
		}
	}
	
	public void keyReady(T key) {
		this.key = key;
		Iterator<Consumer<T>> it = tasks.iterator();
		while (it.hasNext()) {
			Consumer<T> task = it.next();
			it.remove();
			task.accept(key);
		}
	}
}
