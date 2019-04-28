package net.mgsx.ld44.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.BSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class CurrencyCurve {
	public static final int BUFFER_SIZE = 10;
	private static final Vector2 normal = new Vector2();
	public BSpline<Vector2> spline = new BSpline<Vector2>();
	public int index;
	public Color color;
	public Vector2[] controlPoints = new Vector2[BUFFER_SIZE];
	public int controlPointsLength = 0;
	public Array<Vector2> renderVertices = new Array<Vector2>();
	private float thickness = 1;
	public int renderVerticesCount = 0;
	private boolean valid;
	
	public static final int stepsPerSegment = 30;

	public CurrencyCurve() {
		for(int i=0 ; i<controlPoints.length ; i++) controlPoints[i] = new Vector2();
		for(int i=0 ; i<controlPoints.length*stepsPerSegment*2 ; i++) renderVertices.add(new Vector2());
	}
	
	public CurrencyCurve set(int index, Color color) {
		this.index = index;
		this.color = color;
		return this;
	}
	
	public void add(Vector2 point){
		
		// loop
		if(controlPointsLength >= controlPoints.length){
			Vector2 first = controlPoints[0];
			for(int i=0 ; i<controlPoints.length-1 ; i++){
				controlPoints[i] = controlPoints[i+1];
			}
			controlPointsLength--;
			controlPoints[controlPointsLength] = first;
		}
		controlPoints[controlPointsLength].set(point);
		controlPointsLength++;
		spline.set(controlPoints, 3, false);
		
		valid = false;
	}
	
	public void update(){
		thickness = .03f;
		if(!valid){
			valid = true;
			renderVerticesCount = 0;
			for(int i=0 ; i<controlPointsLength ; i++){
				for(int j=0 ; j<stepsPerSegment ; j++){
					float t = (((float)j / (float)stepsPerSegment) + (float)i) / (float)controlPointsLength;
					Vector2 a = renderVertices.get((i * stepsPerSegment + j)*2);
					Vector2 b = renderVertices.get((i * stepsPerSegment + j)*2+1);
					getPosition(a, t);
					
					getNormal(normal, t);
					normal.set(normal.y, -normal.x);
					
					b.set(a).mulAdd(normal, thickness);
					renderVerticesCount+=2;
				}
			}
		}
	}
	
	public Vector2 getPosition(Vector2 result, float t){
		return spline.valueAt(result, t);
	}
	public Vector2 getNormal(Vector2 result, float t){
		return spline.derivativeAt(result, t);
	}
}
