CREATE OR REPLACE FUNCTION auto_time()
    RETURNS TRIGGER AS
$$
BEGIN
    IF
        TG_OP = 'INSERT' THEN
        NEW.created_at = now();
        NEW.updated_at
            = now();
    ELSIF
        TG_OP = 'UPDATE' THEN
        NEW.updated_at = now();
    END IF;
    RETURN NEW;
END;
$$
    LANGUAGE 'plpgsql';

DROP TABLE IF EXISTS chat_history;
CREATE TABLE chat_history
(
    id         BIGSERIAL PRIMARY KEY,             -- 自增用户ID
    user_id    BIGINT                   NOT NULL, -- 用户ID
    title      VARCHAR                  NOT NULL, -- 标题
    context    json                     not null,--                                                NOT NULL UNIQUE, -- 邮箱，必须唯一
    created_at TIMESTAMP WITH TIME ZONE NOT NULL, -- 用户创建时间
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL  -- 用户更新时间
);
CREATE
    OR REPLACE TRIGGER auto_chat_history_time
    BEFORE INSERT OR
        UPDATE
    ON chat_history
    FOR EACH ROW
EXECUTE PROCEDURE auto_time();