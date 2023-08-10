local key = KEYS[1]
local value = ARGV[1]
local expire = ARGV[2]
-- 存储结果为  uuid:计数器
if (redis.call("exists",key)==0 or redis.call("hexists",key,value)==1) then
    redis.call("hincrby",key,value,1)
    redis.call("expire",key,1000)
    return 1
else
    return 0
end


