CREATE TABLE IF NOT EXISTS `order` (
    id VARCHAR(64) PRIMARY KEY,
    `value` VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS item (
    sku_id VARCHAR(64) PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS live_session (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    session_code VARCHAR(64),
    anchor_name VARCHAR(128),
    operator VARCHAR(128),
    status VARCHAR(32),
    planned_start_time VARCHAR(32),
    planned_end_time VARCHAR(32),
    actual_start_time VARCHAR(32),
    actual_end_time VARCHAR(32),
    gmt_create TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    gmt_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sku (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT,
    sku_id VARCHAR(64),
    sku_name VARCHAR(255),
    main_image VARCHAR(1024),
    category VARCHAR(128),
    brand VARCHAR(128),
    price VARCHAR(64),
    stock VARCHAR(64),
    gmt_create TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    gmt_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS alert (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT,
    alert_type VARCHAR(64),
    level VARCHAR(32),
    sku_id VARCHAR(64),
    message VARCHAR(1000),
    handled VARCHAR(8) DEFAULT '0',
    handler VARCHAR(128),
    gmt_create TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    gmt_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS danmaku (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT,
    content VARCHAR(2000),
    sender_nick VARCHAR(128),
    intent VARCHAR(64),
    answer VARCHAR(2000),
    aigc BOOLEAN,
    human_confirmed BOOLEAN,
    replied BOOLEAN,
    gmt_create TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    gmt_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS compliance_result (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT,
    sku_id BIGINT,
    sku_name VARCHAR(255),
    check_content VARCHAR(4000),
    passed BOOLEAN,
    hit_count INT,
    gmt_create TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    gmt_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS script (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT,
    sku_id VARCHAR(64),
    title VARCHAR(255),
    content CLOB,
    ai_generated BOOLEAN,
    status VARCHAR(32),
    gmt_create TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    gmt_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS script_node (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    script_id BIGINT,
    node_index INT,
    node_type VARCHAR(64),
    content CLOB,
    keywords VARCHAR(1000),
    sort_order INT,
    status VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS price_compare (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT,
    sku_id BIGINT,
    sku_name VARCHAR(255),
    our_price VARCHAR(64),
    competitor_data CLOB,
    price_advantage VARCHAR(255),
    suggestion VARCHAR(2000),
    gmt_create TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    gmt_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS jargon_translate (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    jargon VARCHAR(255),
    translation VARCHAR(2000),
    category VARCHAR(128),
    aigc BOOLEAN,
    gmt_create TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    gmt_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS script_generate (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT,
    content CLOB,
    aigc BOOLEAN,
    gmt_create TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    gmt_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT,
    title VARCHAR(255),
    summary CLOB,
    metrics_data CLOB,
    highlights CLOB,
    improvements CLOB,
    gmt_create TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    gmt_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_sku_session ON sku(session_id);
CREATE INDEX IF NOT EXISTS idx_alert_session ON alert(session_id);
CREATE INDEX IF NOT EXISTS idx_danmaku_session ON danmaku(session_id);
