/*
 * MIT License
 *
 * Copyright (c) 2020, 2025 Mark Schmieder
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
 * This file is part of the FxPhysics Library
 *
 * You should have received a copy of the MIT License along with the FxPhysics
 * Library. If not, see <https://opensource.org/licenses/MIT>.
 *
 * Project: https://github.com/mhschmieder/fxphysics
 */
package com.mhschmieder.fxphysicsgui.dialog;

import com.mhschmieder.fxcontrols.util.MessageFactory;
import com.mhschmieder.jcommons.util.ClientProperties;
import com.mhschmieder.jphysics.DistanceUnit;
import javafx.geometry.Point2D;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class PhysicsDialogUtilities {

    /**
     * The default constructor is disabled, as this is a static utilities class.
     */
    private PhysicsDialogUtilities() {}

    // NOTE: Point2D is immutable, so this method must return the confirmed
    // coordinates vs. a status. This isn't a problem though, as the default
    // choice is passed in, so a Cancel action simply returns the initial
    // coordinates unchanged.
    public static Point2D showConfirmCoordinatesDialog( final String title,
                                                        final ClientProperties clientProperties,
                                                        final Point2D coordinatesCandidate,
                                                        final DistanceUnit distanceUnit ) {
        final String masthead = MessageFactory.getConfirmCoordinatesMasthead();
        final ConfirmCoordinatesDialog confirmCoordinatesDialog =
                                                                new ConfirmCoordinatesDialog( title,
                                                                                              masthead,
                                                                                              clientProperties,
                                                                                              coordinatesCandidate );
        confirmCoordinatesDialog.setDistanceUnit( distanceUnit );
        final Optional< ButtonType > response = confirmCoordinatesDialog.showModalDialog();

        // Get the Button Type that was pressed, but use standard
        // object-oriented comparisons as Lambda Expressions do not give us
        // the flexibility of exiting this method directly or of sharing
        // common code after initial special-case handling of the three user
        // options ("Yes", "No", and "Cancel") and their variants.
        final ButtonType buttonType = response.get();

        // Handle the full enumeration of potential responses.
        Point2D coordinates = coordinatesCandidate;
        
        switch ( buttonType.getButtonData() ) {
        case HELP:
        case HELP_2:
        case BACK_PREVIOUS:
        case CANCEL_CLOSE:
            // These options equate to cancellation.
            // If the user cancels, return the initial coordinates.
            break;
        case NO:
            // This case is currently unsupported, but would be like a
            // Cancel.
            break;
        case APPLY:
        case FINISH:
        case NEXT_FORWARD:
        case OK_DONE:
        case YES:
            // Sync the data model to the final edits before querying them.
            confirmCoordinatesDialog.updateModel();

            // Return the newly confirmed coordinates.
            coordinates = confirmCoordinatesDialog.getCoordinatesCandidate();
            
            break;
        case BIG_GAP:
        case SMALL_GAP:
        case LEFT:
        case RIGHT:
        case OTHER:
            // It is unlikely that these cases will ever be called, but it
            // is safest to treat them like a Cancel.
            break;
        default:
            break;
        }
        
        return coordinates;
    }
}
