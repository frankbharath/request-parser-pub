CREATE EXTENSION IF NOT EXISTS citext WITH SCHEMA public;

CREATE INDEX IF NOT EXISTS property_search_index ON rm_property USING gin(propertysearch_tsv);

CREATE FUNCTION property_trigger() RETURNS trigger AS $$
begin
  new.propertysearch_tsv :=
     setweight(to_tsvector('pg_catalog.english', coalesce(new.propertyname,'')), 'A') ||
     setweight(to_tsvector('pg_catalog.english', coalesce(new.city,'')), 'A') || 
     setweight(to_tsvector('pg_catalog.english', coalesce(new.postal,'')), 'B') ||
     setweight(to_tsvector('pg_catalog.english', coalesce(new.addressline_1,'')), 'C') ||
     setweight(to_tsvector('pg_catalog.english', coalesce(new.addressline_2,'')), 'C');
  return new;
end
$$ LANGUAGE plpgsql;

CREATE TRIGGER tsvectorupdateforproperty BEFORE INSERT OR UPDATE
    ON rm_property FOR EACH ROW EXECUTE PROCEDURE property_trigger();