/**
 * MIT License
 *
 * Copyright (c) 2020, 2023 Mark Schmieder
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * This file is part of the FxPhysicsGui Library
 *
 * You should have received a copy of the MIT License along with the
 * FxPhysicsGui Library. If not, see <https://opensource.org/licenses/MIT>.
 *
 * Project: https://github.com/mhschmieder/fxphysicsgui
 */
package com.mhschmieder.fxphysicsgui.control;

import com.mhschmieder.commonstoolkit.util.ClientProperties;
import com.mhschmieder.fxguitoolkit.control.TextSelector;
import com.mhschmieder.physicstoolkit.TemperatureUnit;

/**
 * The Temperature Units selector supports all Temperature Units that are
 * currently implemented in our core Physics Library.
 * <p>
 * TODO: Redo as an enum-based XComboBox that provides a List Cell Factory.
 */
public final class TemperatureUnitSelector extends TextSelector {

    private static final String[] TEMPERATURE_UNITS = new String[] {
                                                                     TemperatureUnit.KELVIN
                                                                             .toCanonicalString(),
                                                                     TemperatureUnit.CELSIUS
                                                                             .toCanonicalString(),
                                                                     TemperatureUnit.FAHRENHEIT
                                                                             .toCanonicalString() };

    public TemperatureUnitSelector( final ClientProperties clientProperties,
                                    final boolean applyToolkitCss,
                                    final TemperatureUnit temperatureUnitDefault ) {
        // Always call the superclass constructor first!
        super( clientProperties,
               "Supported Temperature Units", //$NON-NLS-1$
               applyToolkitCss,
               false,
               false,
               TEMPERATURE_UNITS.length,
               temperatureUnitDefault.toCanonicalString(),
               TEMPERATURE_UNITS );
    }

    public TemperatureUnit getTemperatureUnit() {
        return TemperatureUnit.fromCanonicalString( getTextValue() );
    }

    public void setTemperatureUnit( final TemperatureUnit temperatureUnit ) {
        setTextValue( temperatureUnit.toCanonicalString() );
    }

}