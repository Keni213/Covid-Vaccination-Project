package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.Property;

import java.util.List;


public class MarketValueAverage implements Average {
    @Override
    public int getAverage(List<Property> properties) {

        double totalValue = 0.0;
        int amount = 0;

        for (Property p : properties) {
            //continue if the market value record is empty
            if (p.getMarketValue() == null || p.getMarketValue().equals("")) continue;

            try {
                double value = Double.parseDouble(p.getMarketValue());
                totalValue += value;
                amount++;
            } catch (NumberFormatException ignored) {}
        }

        return amount == 0? 0: ((int) totalValue / amount);
    }

    @Override
    public String toString() {
        return "MarketValue";
    }
}
