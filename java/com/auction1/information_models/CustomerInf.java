package com.auction1.information_models;

import com.auction1.models.Customer;
import com.auction1.models.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInf {

    private Customer customer;
    private List<Location> location;

}
