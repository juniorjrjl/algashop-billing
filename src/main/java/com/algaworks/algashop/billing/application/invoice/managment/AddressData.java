package com.algaworks.algashop.billing.application.invoice.managment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressData {
	@NotNull
	private String street;
	@NotNull
	private String number;
	@Nullable
	private String complement;
	@NotNull
	private String neighborhood;
	@NotNull
	private String city;
	@NotNull
	private String state;
	@NotNull
	private String zipCode;
}