CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    user_origin_id BIGINT,
    user_destination_id BIGINT,
    amount REAL,
    CONSTRAINT fk_user_origin_transactions
              FOREIGN KEY(user_origin_id)
        	  REFERENCES user_client(id),
    CONSTRAINT fk_user_dest_transactions
          FOREIGN KEY(user_destination_id)
    	  REFERENCES user_client(id));
