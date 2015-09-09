package moe.mzry.ilmare.service;

/**
 * Callback
 */
public interface Callback<T> {
    void apply(T data);
}
