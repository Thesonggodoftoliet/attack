package com.littlepants.attack.attackweb.validation;

import lombok.Data;
import lombok.experimental.Delegate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationList<E> implements List<E> {
    @Delegate
    @Valid
    public List<E> list = new ArrayList<>();

    @Override
    public String toString(){
        return list.toString();
    }
}
