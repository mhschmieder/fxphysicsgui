/*
 * MIT License
 *
 * Copyright (c) 2020, 2026 Mark Schmieder. All rights reserved.
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
 * This file is part of the FxAcoustics Library
 *
 * You should have received a copy of the MIT License along with the
 * FxAcoustics Library. If not, see <https://opensource.org/licenses/MIT>.
 *
 * Project: https://github.com/mhschmieder/fxacoustics
 */
package com.mhschmieder.fxphysicsgui.swing;

import com.mhschmieder.jacoustics.FrequencySignalUtilities;
import com.mhschmieder.jgui.component.DataViewXComponent;

import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * {@code FrequencyRangeInformationTable} is a specialization of
 * {@link DataViewXComponent} that presents the current Frequency Range related
 * values used for the most recent Sound Field prediction.
 * <p>
 * As this component hosts a read-only parameter set, it needs one number
 * formatter for numbers and another for percents, but no number parsers.
 * <p>
 * Furthermore, this component has a specific style applied, for blending in
 * with the background (as opposed to usual black text on white background) and
 * skipping the horizontal and vertical grid lines.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public final class FrequencyRangeInformationTable extends DataViewXComponent {
    /**
     *
     */
    private static final long   serialVersionUID           = 345263636179115540L;

    // Declare strings for the static part of the table data formatting.
    public static final String  RELATIVE_BANDWIDTH_LABEL   = "Relative Bandwidth";               //$NON-NLS-1$
    public static final String  CENTER_FREQUENCY_LABEL     = "Center Frequency";                 //$NON-NLS-1$
    public static final String  START_FREQUENCY_LABEL      = "Start Frequency";                  //$NON-NLS-1$
    public static final String  STOP_FREQUENCY_LABEL       = "Stop Frequency";                   //$NON-NLS-1$

    private static final String BANDWIDTH_UNITS            = " octave";                          //$NON-NLS-1$

    // Declare default formatted data for each table row.
    // NOTE: Unless we modify FrequencyRange.java, it is hard to special
    // case for not pre-computing the start and stop frequencies, so we just
    // null them all out.
    private static final String RELATIVE_BANDWIDTH_DEFAULT = " Not Available";                   //$NON-NLS-1$
    private static final String CENTER_FREQUENCY_DEFAULT   = " Not Available";                   //$NON-NLS-1$
    private static final String START_FREQUENCY_DEFAULT    = " Not Available";                   //$NON-NLS-1$
    private static final String STOP_FREQUENCY_DEFAULT     = " Not Available";                   //$NON-NLS-1$

    // Declare the array of column names to be displayed in the table header.
    // NOTE: These are all empty strings, but are necessary to imply table
    // width.
    private final Object[]      _columnNames               = { "" };                             //$NON-NLS-1$

    // Declare an array of row data to be displayed in the rows of the table.
    private final Object[][]    _rowData                   =
                                         {
                                           {
                                             RELATIVE_BANDWIDTH_LABEL
                                                     + RELATIVE_BANDWIDTH_DEFAULT },
                                           { CENTER_FREQUENCY_LABEL + CENTER_FREQUENCY_DEFAULT },
                                           { START_FREQUENCY_LABEL + START_FREQUENCY_DEFAULT },
                                           { STOP_FREQUENCY_LABEL + STOP_FREQUENCY_DEFAULT } };

    // Cache the start, stop and center frequencies and make them available
    // after prediction response file is closed.
    public String               _relativeBandwidth         = RELATIVE_BANDWIDTH_DEFAULT;
    public String               _centerFrequency           = CENTER_FREQUENCY_DEFAULT;
    public String               _startFrequency            = START_FREQUENCY_DEFAULT;
    public String               _stopFrequency             = STOP_FREQUENCY_DEFAULT;

    /**
     * Number format cache used for locale-specific number formatting.
     */
    protected NumberFormat      numberFormat;

    public FrequencyRangeInformationTable() {
        // Always call the superclass constructor first!
        super();

        try {
            initComponent();
        }
        catch ( final Exception ex ) {
            ex.printStackTrace();
        }
    }

    private void initComponent() {
        // First make sure the table components exist and are initialized.
        super.initTable( _rowData, _columnNames, SwingConstants.LEFT, false );

        // Initialize the Number Formatters, using the host's default Locale.
        initNumberFormatters( Locale.getDefault() );

        // This is generally the second-narrowest table, but attempt to force a
        // size that accommodates its largest values.
        setPreferredSize( new Dimension( 160, 100 ) );

        // Ensure that there are valid initial defaults.
        reset();
    }

    /**
     * Initializes and caches the number formatters, as the system-level query
     * for the host's default locale is expensive and can cause a freeze-up.
     * <p>
     * The method is declared final, as any derived classes should avoid
     * unwanted side effects and simply write their own number formatter
     * initialization method that resets precision to what is preferred in the
     * context of that tighter scope. These defaults are fairly common ones.
     *
     * @param locale
     *            The locale to use for number formatting purposes
     *
     * @version 1.0
     */
    private void initNumberFormatters( final Locale locale ) {
        // Cache the number formats so that we don't have to get information
        // about locale from the OS each time we format a number.
        numberFormat = NumberFormat.getNumberInstance( locale );
    }

    public void reset() {
        table.setValueAt( RELATIVE_BANDWIDTH_LABEL + RELATIVE_BANDWIDTH_DEFAULT, 0, 0 );
        table.setValueAt( CENTER_FREQUENCY_LABEL + CENTER_FREQUENCY_DEFAULT, 1, 0 );
        table.setValueAt( START_FREQUENCY_LABEL + START_FREQUENCY_DEFAULT, 2, 0 );
        table.setValueAt( STOP_FREQUENCY_LABEL + STOP_FREQUENCY_DEFAULT, 3, 0 );

        // Force a repaint event, to display/update the new table values.
        repaint();
    }

    // Update the cached Frequency Range data.
    // NOTE: This method is generally called with a prediction response.
    @SuppressWarnings("nls")
    public void setFrequencyRange( final double startFrequency,
                                   final double stopFrequency,
                                   final String relativeBandwidth,
                                   final double centerFrequency ) {
        final String sStartFrequency = FrequencySignalUtilities
                .getFormattedFrequency( startFrequency, numberFormat );
        final String sStopFrequency = FrequencySignalUtilities
                .getFormattedFrequency( stopFrequency, numberFormat );
        final String sCenterFrequency = FrequencySignalUtilities
                .getFormattedFrequency( centerFrequency, numberFormat );

        _relativeBandwidth = " = " + relativeBandwidth + BANDWIDTH_UNITS;
        _centerFrequency = " = " + sCenterFrequency;
        _startFrequency = " = " + sStartFrequency;
        _stopFrequency = " = " + sStopFrequency;

        // Update the associated labels in the table.
        update();
    }

    protected void update() {
        table.setValueAt( RELATIVE_BANDWIDTH_LABEL + _relativeBandwidth, 0, 0 );
        table.setValueAt( CENTER_FREQUENCY_LABEL + _centerFrequency, 1, 0 );
        table.setValueAt( START_FREQUENCY_LABEL + _startFrequency, 2, 0 );
        table.setValueAt( STOP_FREQUENCY_LABEL + _stopFrequency, 3, 0 );

        // Force a repaint event, to display/update the new table values.
        repaint();
    }
}
