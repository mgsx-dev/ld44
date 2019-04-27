package net.mgsx.ld44.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import net.mgsx.ld44.assets.GameAssets;
import net.mgsx.ld44.model.CurrencyCurve;

public class CurveActor extends Actor {
	private ShapeRenderer renderer;
	private CurrencyCurve curve;
	private TextureRegion region;
	
	
	public CurveActor(ShapeRenderer renderer, CurrencyCurve curve) {
		super();
		this.renderer = renderer;
		this.curve = curve;
		this.region = GameAssets.i.skin.getRegion("white");
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();
		
		renderer.setProjectionMatrix(batch.getProjectionMatrix());
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.WHITE);
		for(int i=0 ; i<curve.controlPointsLength-1 ; i++){
			// renderer.rectLine(curve.controlPoints[i], curve.controlPoints[i+1], 5);
		}
		for(int i=0 ; i<curve.renderVerticesCount-2 ; i+=2){
			// renderer.rectLine(curve.renderVertices.get(i), curve.renderVertices.get(i+2), 5);
		}
		renderer.flush();
		
		draw(renderer.getRenderer());
		renderer.end();
		batch.begin();
	}
	
	public void draw(ImmediateModeRenderer renderer) {
		renderer.flush();
		region.getTexture().bind(0);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		float u0 = region.getU();
		float u1 = region.getU2();
		float v0 = region.getV2();
		float v1 = region.getV();
		Array<Vector2> slices = curve.renderVertices;
		for(int i=2 ; i<curve.renderVerticesCount ; i+=2){
			Vector2 a = slices.get(i-2);
			Vector2 c = slices.get(i-1);
			Vector2 b = slices.get(i);
			Vector2 d = slices.get(i+1);
			
			// Triangle 1
			renderer.texCoord(u0, v0);
			renderer.color(curve.color);
			renderer.vertex(a.x, a.y, 0);
			
			renderer.color(curve.color);
			renderer.texCoord(u1, v0);
			renderer.vertex(b.x, b.y, 0);
			
			renderer.texCoord(u0, v1);
			renderer.color(curve.color);
			renderer.vertex(c.x, c.y, 0);
			
			
			// Triangle 2
			renderer.color(curve.color);
			renderer.texCoord(u1, v0);
			renderer.vertex(b.x, b.y, 0);
			
			renderer.color(curve.color);
			renderer.texCoord(u0, v1);
			renderer.vertex(c.x, c.y, 0);
			
			renderer.color(curve.color);
			renderer.texCoord(u1, v1);
			renderer.vertex(d.x, d.y, 0);
		}
		renderer.flush();
	}
}
