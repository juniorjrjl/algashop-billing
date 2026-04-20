create table public.credit_cards (
    id uuid not null primary key,
    brand varchar(255),
    created_at timestamp(6) with time zone,
    customer_id uuid,
    expiration_month integer,
    expiration_year integer,
    gateway_code varchar(255),
    last_numbers varchar(255)
);

create index idx_credit_card_customer_id on public.credit_cards (customer_id);
