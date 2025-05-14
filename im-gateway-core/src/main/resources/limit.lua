-- limit.lua
-- 时间戳记录在 Zset 中，维护一个窗口，超时的删除
local key = KEYS[1]
local limit = tonumber(ARGV[1])
local now = tonumber(ARGV[2])
local window = tonumber(ARGV[3])

redis.call("ZADD", key, now, now)
redis.call("ZREMRANGEBYSCORE", key, 0, now - window)

local count = redis.call("ZCARD", key)
redis.call("EXPIRE", key, math.ceil(window / 1000))

if count > limit then
    return 0
else
    return 1
end
