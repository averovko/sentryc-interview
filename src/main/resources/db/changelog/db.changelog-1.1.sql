--liquibase formatted sql

--changeset averovko:1
ALTER TABLE public.sellers
  ADD CONSTRAINT UniqueProducerIdAndSellerInfoIdAndState UNIQUE (producer_id, seller_info_id, state);
--rollback  ALTER TABLE public.sellers DROP CONSTRAINT UniqueProducerIdAndSellerInfoIdAndState;

