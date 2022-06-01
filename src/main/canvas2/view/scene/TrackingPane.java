package canvas2.view.scene;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import canvas2.util.TransformUtil;

/**
 * 指定の{@link Node}を指定の範囲に描画するクラス。<br>
 * ノード配下に登録すること。
 */
public class TrackingPane extends Pane{

	private Node targetNode;

	public TrackingPane(String outer, String inner, Node targetNode)
	{
		super(outer, inner);
		this.targetNode = targetNode;

		this.getInnerNode().add(g2 -> {

			this.targetNode.draw(g2);
		});

	}

	/**
	 * 指定座標を中心にします。
	 */
	public void track(double x, double y, boolean isCenter)
	{
		double tx = -x;
		double ty = -y;

		if(isCenter)
		{
			Rectangle rect = this.getShape().getBounds();
			double centerX = rect.getCenterX();
			double centerY = rect.getCenterY();

			tx += centerX;
			ty += centerY;
		}

		AffineTransform t = this.getInnerNode().getTransform();
		TransformUtil.setTranslate(t, tx, ty);
	}

	/**
	 * 指定座標を中心にします。
	 */
	public void track(Point pos, boolean isCenter)
	{
		this.track(pos.getX(), pos.getY(), isCenter);
	}








}
