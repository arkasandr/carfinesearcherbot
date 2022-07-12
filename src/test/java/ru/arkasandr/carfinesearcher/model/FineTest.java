package ru.arkasandr.carfinesearcher.model;import org.junit.jupiter.api.DisplayName;import org.junit.jupiter.api.Test;import java.time.LocalDateTime;import static java.time.LocalDateTime.now;import static org.assertj.core.api.Assertions.assertThat;import static org.junit.jupiter.api.Assertions.*;@DisplayName("Класс GibddRequest")class FineTest {    @Test    @DisplayName("новый fine корректно создаётся")    void haveCorrectConstructorWithAllArguments() {        Long id = 1L;        Double value = 200.5;        LocalDateTime date = now();        Fine testFine = Fine.builder()                .id(id)                .value(value)                .receiptDate(date)                .build();        assertEquals(200.5, testFine.getValue());        assertThat(testFine)                .hasFieldOrPropertyWithValue("receiptDate", date);    }}