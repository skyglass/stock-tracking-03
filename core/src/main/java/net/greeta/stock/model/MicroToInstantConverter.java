package net.greeta.stock.model;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.time.Instant;

public class MicroToInstantConverter extends StdConverter<String, Instant> {
  public Instant convert(final String value) {
    return Instant.parse(value);
  }
}
