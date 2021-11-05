package main.util;

import java.awt.geom.AffineTransform;

/**
 * {@link AffineTransform}の操作を簡略化するクラス。
 */
public class TransformUtil {

	private TransformUtil()
	{

	}

	public static void setTranslate(AffineTransform t, double x, double y)
	{
		double scaleX = t.getScaleX();
		double scaleY = t.getScaleY();
		double shearX = t.getShearX();
		double shearY = t.getShearY();

		t.setTransform(scaleX, shearY, shearX, scaleY, x, y);
	}

	public static void setScale(AffineTransform t, double x, double y)
	{
		double shearX = t.getShearX();
		double shearY = t.getShearY();
		double translateX = t.getTranslateX();
		double translateY = t.getTranslateY();

		t.setTransform(x, shearY, shearX, y, translateX, translateY);
	}

	public static void scale(AffineTransform t, double scale, double x, double y)
	{
		//現在の中心点
		double startX = x * t.getScaleX();
		double startY = y * t.getScaleY();

		t.scale(scale, scale);

		//スケーリング後の中心点
		double appliedX = x * t.getScaleX();
		double appliedY = y * t.getScaleY();

		//中心点との差(スケーリングしたため、差もスケーリング)
		double moveX = (startX - appliedX) / t.getScaleX();
		double moveY = (startY - appliedY) / t.getScaleY();


		t.translate(moveX, moveY);

	}

}
