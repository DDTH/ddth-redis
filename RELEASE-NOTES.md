ddth-redis release notes
========================

0.3.1 - 2014-04-09
------------------
- New pipeline APIs:
  - `multiGet(...)` and `hashMultiGet(...)`


0.3.0 - 2014-04-08
------------------
- `PoolConfig`: add configurations for `testOnBorrow`, `testOnCreate` and `testWhileIdle`.
- New APIs:
  - `hashMGet(...)`


0.2.2 - 2014-04-02
------------------
- New APIs:
  - `incBy(String, long)` and `decBy(String, long)`
  - `getSet(...)` and `getSetAsBinary(...)`
  - `hashIncBy(String, String, long)` and `hashDecBy(String, String, long)`
- Key expiry enhancements.


0.1.0.1 - 2014-03-23
--------------------
- First release.
