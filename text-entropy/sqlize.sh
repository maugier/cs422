awk "{print \"insert into el values('\"\$1\"',\"\$2\",\"\$3\");\"}" <result-lang |head -n10 |sqlite3 result-lang.sq3
