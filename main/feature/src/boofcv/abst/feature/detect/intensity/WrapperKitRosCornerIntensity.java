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

package boofcv.abst.feature.detect.intensity;

import boofcv.alg.feature.detect.intensity.KitRosCornerIntensity;
import boofcv.core.image.GeneralizedImageOps;
import boofcv.struct.QueueCorner;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSingleBand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Wrapper around children of {@link boofcv.alg.feature.detect.intensity.GradientCornerIntensity}.
 * 
 * @author Peter Abeles
 */
public class WrapperKitRosCornerIntensity<I extends ImageSingleBand,D extends ImageSingleBand> implements GeneralFeatureIntensity<I,D> {

	ImageFloat32 intensity = new ImageFloat32(1,1);
	Method m;

	public WrapperKitRosCornerIntensity(Class<D> derivType ) {
		try {
			m = KitRosCornerIntensity.class.getMethod("process",ImageFloat32.class,derivType,derivType,derivType,derivType,derivType);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void process(I image , D derivX, D derivY, D derivXX, D derivYY, D derivXY ) {
		if( intensity.width != image.width || intensity.height != image.height ) {
			intensity.reshape(image.width,image.height);
			// zero the image to make sure it's borders values are zero
			GeneralizedImageOps.fill(intensity, 0);
		}

		try {
			m.invoke(null,intensity,derivX,derivY,derivXX,derivYY,derivXY);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ImageFloat32 getIntensity() {
		return intensity;
	}

	@Override
	public QueueCorner getCandidates() {
		return null;
	}

	@Override
	public boolean getRequiresGradient() {
		return true;
	}

	@Override
	public boolean getRequiresHessian() {
		return true;
	}

	@Override
	public boolean hasCandidates() {
		return false;
	}

	/**
	 * There is no ignore border, unless the derivative that it is computed from has an ignore border.
	 */
	@Override
	public int getIgnoreBorder() {
		return 0;
	}
}
