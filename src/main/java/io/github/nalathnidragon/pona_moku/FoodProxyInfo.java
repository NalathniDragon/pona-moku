package io.github.nalathnidragon.pona_moku;

public class FoodProxyInfo {
	public final float hunger;
	public final float saturationMultiplier;
	public final int slices;
	public FoodProxyInfo(float hunger, float saturationMultiplier, int slices)
	{
		this.hunger=hunger;
		this.saturationMultiplier=saturationMultiplier;
		this.slices=slices;
	}
}
