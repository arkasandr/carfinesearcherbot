package ru.arkasandr.carfinesearcher.service.message.dto;import lombok.AllArgsConstructor;import lombok.Builder;import lombok.Data;import lombok.NoArgsConstructor;import java.util.UUID;@Data@Builder@AllArgsConstructor@NoArgsConstructorpublic class CaptchaMessageRequestDto {    private UUID uuid;    private byte[] captcha;    private String message;}