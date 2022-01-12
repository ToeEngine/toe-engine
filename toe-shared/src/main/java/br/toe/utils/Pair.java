package br.toe.utils;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class Pair <K, V> {

    private K key;
    private V value;

    public String toString() {
        return "%s = %s".formatted(key, value);
    }
}