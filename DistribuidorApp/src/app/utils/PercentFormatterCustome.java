package app.utils;

import java.text.DecimalFormat;

import com.github.mikephil.charting.utils.ValueFormatter;


public class PercentFormatterCustome implements ValueFormatter {

    protected DecimalFormat mFormat;

    public PercentFormatterCustome() {
        mFormat = new DecimalFormat("###,###,###.00");
    }

    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value) + " %";
    }
}
