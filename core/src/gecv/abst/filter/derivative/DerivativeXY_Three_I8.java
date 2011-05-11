/*
 * Copyright 2011 Peter Abeles
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package gecv.abst.filter.derivative;

import gecv.alg.filter.derivative.GradientThree;
import gecv.struct.image.ImageSInt16;
import gecv.struct.image.ImageUInt8;


/**
 * @author Peter Abeles
 */
public class DerivativeXY_Three_I8 implements DerivativeXY<ImageUInt8, ImageSInt16> {

	@Override
	public void process(ImageUInt8 inputImage , ImageSInt16 derivX, ImageSInt16 derivY) {
		GradientThree.deriv_I8(inputImage, derivX, derivY);
	}

	@Override
	public int getBorder() {
		return 1;
	}
}