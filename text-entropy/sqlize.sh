awk "{print \"insert into el values('\"\$1\"',\"\$2\",\"\$3\");\"}" <result-lang |sqlite3 result-lang.sq3
