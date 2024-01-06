package lk.ijse.laboratory.Util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

public class calculateAge {
    public static String calAge(Date Bd){
        String age = null;
        LocalDate today = LocalDate.now();
        LocalDate dob = Bd.toLocalDate();
        int ag = Period.between(dob, today).getYears();
        if (ag >= 1) {
            age = String.valueOf(ag);
        } else {
            age = String.valueOf(Period.between(dob, today).getMonths());
        }
        return age;

    }
}
