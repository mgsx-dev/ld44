package net.mgsx.ecs;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.Annotation;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import net.mgsx.ecs.reflection.Asset;
import net.mgsx.ecs.reflection.Auto;

public class KitEngine extends PooledEngine
{
	private final ObjectMap<Class, Object> sharedData = new ObjectMap<Class, Object>();
	
	private AssetManager assetManager;
	private boolean assetsLoaded;
	
	public KitEngine(AssetManager assetManager) {
		super();
		this.assetManager = assetManager;
	}
	
	@Override
	public void addSystem(EntitySystem system) {
		for(Field field : ClassReflection.getFields(system.getClass())){
			if(field.getDeclaredAnnotation(Auto.class) != null){
				Object value = getSystem(field.getType());
				if(value == null){
					value = getSharedData(field.getType());
				}
				try {
					field.set(system, value);
				} catch (ReflectionException e) {
					throw new GdxRuntimeException(e);
				}
			};
			Annotation a = field.getDeclaredAnnotation(Asset.class);
			if(a != null){
				Asset asset = a.getAnnotation(Asset.class);
				assetManager.load(asset.value(), field.getType());
				
			}
		}
		super.addSystem(system);
	}
	@Override
	public void removeSystem(EntitySystem system) {
		for(Field field : ClassReflection.getFields(system.getClass())){
			if(field.getDeclaredAnnotation(Auto.class) != null){
				try {
					field.set(system, null);
				} catch (ReflectionException e) {
					throw new GdxRuntimeException(e);
				}
			};
		}
		super.removeSystem(system);
	}
	
	@Override
	public void update(float deltaTime) {
		if(!assetsLoaded && assetManager.update()){
			assetsLoaded = true;
			for(EntitySystem system : getSystems()){
				for(Field field : ClassReflection.getFields(system.getClass())){
					Annotation a = field.getDeclaredAnnotation(Asset.class);
					if(a != null){
						Asset asset = a.getAnnotation(Asset.class);
						Object value = assetManager.get(asset.value(), field.getType());
						try {
							field.set(system, value);
						} catch (ReflectionException e) {
							throw new GdxRuntimeException(e);
						}
					}
				}
			}
		}
		super.update(deltaTime);
	}
	
	public Object getSharedData(Class type) {
		return sharedData.get(type);
	};
	public void setSharedData(Class type, Object object) {
		sharedData.put(type, object);
	};
	public void setSharedData(Object object) {
		setSharedData(object.getClass(), object);
	}

	public void addObject(Object object) {
		setSharedData(object);
	};
}
