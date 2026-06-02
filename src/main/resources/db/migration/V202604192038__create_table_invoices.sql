create table invoices (
    id uuid not null primary key,
    created_at timestamp(6) with time zone,
    created_by_user_id uuid,
    last_modified_at timestamp(6) with time zone,
    last_modified_by_user_id uuid,
    version bigint not null,
    cancel_reason varchar(255),
    canceled_at timestamp(6) with time zone,
    customer_id uuid,
    expires_at timestamp(6) with time zone,
    invoice_status varchar(255) check ((invoice_status in ('PAID','UNPAID','CANCELED'))),
    issued_at timestamp(6) with time zone,
    order_id varchar(255),
    paid_at timestamp(6) with time zone,
    payer_address_city varchar(255),
    payer_address_complement varchar(255),
    payer_address_neighborhood varchar(255),
    payer_address_number varchar(255),
    payer_address_state varchar(255),
    payer_address_street varchar(255),
    payer_address_zip_code varchar(255),
    payer_document varchar(255),
    payer_email varchar(255),
    payer_full_name varchar(255),
    payer_phone varchar(255),
    total_amount numeric(38,2),
    payment_settings_id uuid
);

create index idx_invoice_customer_id on invoices (customer_id);
create index idx_invoice_order_id on invoices (order_id);
create unique index idx_invoice_payment_settings_id on invoices (payment_settings_id);

alter table invoices add constraint fk_invoice_payment_settings_id foreign key (payment_settings_id) references payment_settings(id);

create table invoice_line_items (
    invoice_id uuid not null,
    "items_amount" numeric(38,2),
    "items_name" varchar(255),
    "items_number" integer
);

create index idx_invoice_line_item_invoice_id on invoice_line_items (invoice_id);
alter table invoice_line_items add constraint fk_invoice_line_item_invoice_id foreign key (invoice_id) references invoices(id);
