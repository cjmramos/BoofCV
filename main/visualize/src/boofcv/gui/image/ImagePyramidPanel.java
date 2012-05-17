/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://boofcv.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package boofcv.gui.image;

import boofcv.alg.distort.DistortImageOps;
import boofcv.alg.interpolate.InterpolatePixel;
import boofcv.alg.interpolate.TypeInterpolate;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.factory.interpolate.FactoryInterpolation;
import boofcv.gui.ListDisplayPanel;
import boofcv.struct.image.ImageSingleBand;
import boofcv.struct.pyramid.ImagePyramid;

import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Displays an {@link ImagePyramid} by listing each of its layers and showing them one at a time.
 * Each layer can be scaled up to the size of the original layer if desired.
 *
 * @author Peter Abeles
 */
@SuppressWarnings({"unchecked"})
public class ImagePyramidPanel<T extends ImageSingleBand> extends ListDisplayPanel {

	// the image pyramid.
	ImagePyramid<T> pyramid;
	// interpolation used for upscaling
	InterpolatePixel<T> interp;
	// temporary storage for upscaled image
	T upscale;
	// if each layer should be scaled up to the original resolution or not
	boolean scaleUp;

	public ImagePyramidPanel(ImagePyramid<T> pyramid, boolean scaleUp) {
		set(pyramid,scaleUp);
		render();
		setPreferredSize(new Dimension(pyramid.getWidth(0),pyramid.getHeight(0)));
	}

	public ImagePyramidPanel() {
	}

	public void set(ImagePyramid<T> pyramid, boolean scaleUp) {
		this.pyramid = pyramid;
		this.scaleUp = scaleUp;
	}

	/**
	 * Redraws each layer
	 */
	public void render() {
		reset();

		if( pyramid == null )
			return;

		if( scaleUp ) {
			scaleUpLayers();
		} else {
			doNotScaleLayers();
		}
	}

	private void doNotScaleLayers() {
		int N = pyramid.getNumLayers();

		for( int i = 0; i < N; i++ ) {
			BufferedImage b = ConvertBufferedImage.convertTo(pyramid.getLayer(i),null);
			addImage(b,String.format("%5.2f",pyramid.getScale(i)));
		}
	}

	private void scaleUpLayers() {
		T l = pyramid.getLayer(0);
		if( upscale == null ) {
			interp = (InterpolatePixel<T>) FactoryInterpolation.nearestNeighborPixel(l.getClass());
			upscale = (T)l._createNew(l.width,l.height);
		} else {
			upscale.reshape(l.width,l.height);
		}

		int N = pyramid.getNumLayers();

		for( int i = 0; i < N; i++ ) {
			DistortImageOps.scale(pyramid.getLayer(i),upscale, TypeInterpolate.NEAREST_NEIGHBOR);
			BufferedImage b = ConvertBufferedImage.convertTo(upscale,null);
			addImage(b,String.format("%5.2f",pyramid.getScale(i)));
		}
	}
}
