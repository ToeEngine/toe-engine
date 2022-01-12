package br.toe.utils;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class Tuple<A, B, C> {

    private A a;
    private B b;
    private C c;

    public String toString() {
        return "(%s, %s, %s)".formatted(a, b, c);
    }
}
