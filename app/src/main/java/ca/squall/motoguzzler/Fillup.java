package ca.squall.motoguzzler;

import com.orm.SugarRecord;
import com.orm.dsl.NotNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by charles on 2016-08-10.
 */
public class Fillup extends SugarRecord {


    @NotNull
    private int odometer;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private BigDecimal cost;

    @NotNull
    private Date date;

    // Default constructor is necessary for SugarRecord
    public Fillup() {
        this(0, new BigDecimal(0.00), new BigDecimal(0.00), new Date());
    }


    public Fillup(int odometer, BigDecimal amount, BigDecimal cost) {
        this(odometer, amount, cost, new Date());
    }

    public Fillup(int odometer, BigDecimal amount, BigDecimal cost, Date date) {
        Fillup prevFillup;

        this.odometer = odometer;
        this.amount = amount;
        this.cost = cost;
        this.date = date;

        setDecimalScale();
    }

    public static BigDecimal getAvgFuelEconomy() {

        List<Fillup> fillups = Fillup.listAll();

        if (fillups.size() < 2) {
            return new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        }

        BigDecimal total = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        Fillup fillup;
        for (int i = 0; i < fillups.size(); i++) {
            fillup = fillups.get(i);
            total = total.add(fillup.getFuelEconomy());
        }
        BigDecimal count = new BigDecimal(fillups.size() - 1).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        return total.divide(count, 2, BigDecimal.ROUND_HALF_DOWN);
    }

    private static long count() {
        return Fillup.count(Fillup.class);
    }

    public static List<Fillup> listAll() {
        return Fillup.listAll(Fillup.class);
    }

    public static BigDecimal getFuelEconomyChange() {
        List<Fillup> fillups = Fillup.listAll();

        if (Fillup.count() < 2) {
            return new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        }

        Fillup lastFillup = fillups.get(fillups.size() - 1);
        Fillup secondLastFillup = fillups.get(fillups.size() - 2);

        return lastFillup.getFuelEconomy().subtract(secondLastFillup.getFuelEconomy());
    }


    @Override
    public long save() {
        Fillup prevFillup = this.previous();

        if (prevFillup != null && prevFillup.getOdometer() >= this.getOdometer()) {
            throw new RuntimeException("New Odometer reading is lesser or equal to older odometer reading.");
        }

        return super.save();
    }

    private void setDecimalScale() {
        this.cost = this.cost.setScale(2, BigDecimal.ROUND_HALF_DOWN);
        this.amount = this.amount.setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

    public int getOdometer() {
        return odometer;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount.setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost.setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getFuelEconomy() {
        BigDecimal fuelEconomy = new BigDecimal(0.00);
        fuelEconomy = fuelEconomy.setScale(2, BigDecimal.ROUND_HALF_DOWN);

        if (Fillup.count(Fillup.class) > 1 && Fillup.first(Fillup.class).getId() != this.getId()) {

            long id = this.getId();
            Fillup prevFillup = this.previous();

            assert prevFillup != null;
            int kmDiff = this.getOdometer() - prevFillup.getOdometer();
            BigDecimal liters = this.getAmount();

            fuelEconomy = new BigDecimal(kmDiff).divide(liters, 2, BigDecimal.ROUND_HALF_DOWN);

        }

        return fuelEconomy;
    }

    private Fillup previous() {

        //No Fillups in DB
        if (Fillup.count(Fillup.class) < 1) {
            return null;
        }

        //New record
        if (this.getId() == null) {
            return Fillup.last(Fillup.class);
        }

        //Get last Fillup with id less than this one.
        List<Fillup> fillups = Fillup.find(Fillup.class, "id < ?", new String[]{"" + this.getId()}, "", "id DESC", "1");
        if (fillups.size() <= 0) {
            return null;
        } else {
            return fillups.get(0);
        }
    }
}
