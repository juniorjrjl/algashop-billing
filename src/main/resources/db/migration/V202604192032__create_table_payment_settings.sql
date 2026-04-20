create table public.payment_settings (
    id uuid not null primary key,
    credit_card_id uuid,
    gateway_code varchar(255),
    payment_method varchar(255) check ((payment_method in ('CREDIT_CARD','GATEWAY_BALANCE')))
);

create index idx_payment_settings_credit_card_id on public.payment_settings (credit_card_id);
alter table public.payment_settings add constraint fk_payment_settings_credit_card_id foreign key (credit_card_id) references public.credit_cards(id);
