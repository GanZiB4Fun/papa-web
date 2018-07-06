package com.ganzib.papa.db.redis;

import com.swzh.news.support.utils.SerializeUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.*;
import java.util.Map.Entry;

@Service
public class BaseRedisDao<K, V> {
    public static final int DEFAULT = 0;
    public static final int DISTINCTDB = 1;
    public static final int CHANNELSTAT = 2;
    public static final int DOCSTAT = 3;
    public static final int USERSTAT = 4;
    public static final int H5IMPORTSTAT = 5;
    public static final int PAGESESSION = 6;
    public static final int USERVALIDREAD = 7;
    public static final int IPSTAT = 8;
    public static final int IPBLACK = 9;
    public static final int IPBLACKUSER = 10;
    public static final int ADVWALLSTAT = 11;
    public static final int USEREXSTAT = 12;
    public static final int COUNTERSTAT = 13;
    public static final int USERONLINESTAT = 14;
    public static final int AUTOUSERDB = 15;
    public static final int USERHEARTDB = 16;
    public static final int PERMLINKDB = 17;
    public static final int USERMININGOPERATOR=18;

    @Resource
    protected RedisTemplate<K, V> redisTemplate;

    /**
     * 设置redisTemplate
     *
     * @param redisTemplate the redisTemplate to set
     */
    public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取 RedisSerializer <br>
     * ------------------------------<br>
     */
    protected RedisSerializer<String> getRedisSerializer() {
        return redisTemplate.getStringSerializer();
    }

    public boolean getbit(final String key, final Long offset, int db) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                return connection.getBit(serKey, offset);
            }
        });
        return result;
    }

    public Long incr(final String key, int db) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                return connection.incr(serKey);
            }
        });
        return result;
    }

    public Long incrBy(final String key, final Long value, int db) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                return connection.incrBy(serKey, value);
            }
        });
        return result;
    }

    public Long hIncrBy(final String key, final String field, final Long delta, int db) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serField = serializer.serialize(field);
                return connection.hIncrBy(serKey, serField, delta);
            }
        });
        return result;
    }

    public Long getCountByKey(final String key, int db) {
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = connection.get(serKey);
                if (serValue == null || serValue.length == 0) {
                    return 0L;
                }
                String value = serializer.deserialize(serValue);
                return Long.valueOf(value);
            }
        });
        return result;
    }

    public Long getDBSize(int db) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                return connection.dbSize();
            }
        });
    }

    public Long delByKey(final String key, int db) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                return connection.del(serKey);
            }
        });
    }

    public boolean hset(final String key, final String field, final String value, int db) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serField = serializer.serialize(field);
                byte[] serValue = serializer.serialize(value);
                return connection.hSet(serKey, serField, serValue);
            }
        });
        return result;
    }

    public Map<String, String> hgetAll(final String key, int db) {
        Map<String, String> map = redisTemplate.execute(new RedisCallback<Map<String, String>>() {
            @Override
            public Map<String, String> doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] key1 = serializer.serialize(key);
                Map<byte[], byte[]> map = connection.hGetAll(key1);
                if (map == null) {
                    return null;
                }
                Map<String, String> mapStat = new HashMap<>(32);
                Iterator<Entry<byte[], byte[]>> iter = map.entrySet().iterator();
                while (iter.hasNext()) {
                    Entry<byte[], byte[]> entry = iter.next();
                    String nKey = serializer.deserialize(entry.getKey());
                    String nvalue = serializer.deserialize(entry.getValue());
                    mapStat.put(nKey, nvalue);
                }
                return mapStat;
            }
        });
        return map;
    }

    public Long sAdd(final String key, final String value, int db) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = serializer.serialize(value);
                return connection.sAdd(serKey, serValue);

            }
        });
    }

    public Boolean exists(final String key, int db) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                return connection.exists(serKey);
            }
        });
    }

    public String sPop(final String key, int db) {
        String value = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = connection.sPop(serKey);
                if (serValue != null && serValue.length > 0) {
                    return serializer.deserialize(serValue);
                } else {
                    return null;
                }
            }
        });
        return value;
    }

    public String rPop(final String key, int db) {
        String value = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = connection.rPop(serKey);
                if (serValue != null && serValue.length > 0) {
                    return serializer.deserialize(serValue);
                } else {
                    return null;
                }
            }
        });
        return value;
    }

    public Boolean hSetNx(final String key, final String field, String value, int db) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serField = serializer.serialize(field);
                byte[] serValue = serializer.serialize(value);
                boolean re = connection.hSetNX(serKey, serField, serValue);
                return re;
            }
        });
    }

    public Set<String> sMember(final String key, int db) {
        Set<String> set = redisTemplate.execute(new RedisCallback<Set<String>>() {
            @Override
            public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                Set<byte[]> set = connection.sMembers(serKey);
                Set<String> sets = new HashSet<String>();
                Iterator<byte[]> iter = set.iterator();
                while (iter.hasNext()) {
                    String setValue = serializer.deserialize(iter.next());
                    sets.add(setValue);
                }
                return sets;
            }
        });
        return set;
    }

    public boolean setNX(final String key, final String value, int db) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = serializer.serialize(value);
                boolean re = connection.setNX(serKey, serValue);
                return re;
            }
        });
    }

    public String hget(final String key, final String field, int db) {
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serField = serializer.serialize(field);
                byte[] serValue = connection.hGet(serKey, serField);
                if (serValue == null || serValue.length == 0) {
                    return null;
                }
                String value = serializer.deserialize(serValue);
                return value;
            }
        });
    }

    public boolean setBit(final String key, final Long offset, final Boolean value, int db) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                boolean re = connection.setBit(serKey, offset, value);
                return re;

            }
        });
    }


    public boolean setBit(final String key, final Long offset, final Boolean value, final Long seconds,int db) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                boolean re = connection.setBit(serKey, offset, value);
                if(re) {
                 connection.expire(serKey, seconds);
                }
                return re;

            }
        });
    }

    public boolean expire(final String key, final Long seconds, int db) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                boolean re = connection.expire(serKey, seconds);
                return re;
            }
        });
    }

    public void setEx(final String key, final Long seconds, final String value, int db) {
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = serializer.serialize(value);
                connection.setEx(serKey, seconds, serValue);
                return null;
            }
        });
    }

    public String set(final byte[] key, final byte[] value, int db) {
        try {
            redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.select(db);
                    connection.set(key, value);
                    return "OK";
                }
            });
        } catch (Exception ex) {
            return null;
        }
        return "OK";
    }

    public String get(final String key, int db) {
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = connection.get(serKey);
                if (serValue == null || serValue.length == 0) {
                    return null;
                }
                return serializer.deserialize(serValue);
            }
        });
    }

    public Long lPush(final String key, final String value, int db) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = serializer.serialize(value);
                return connection.lPush(serKey, serValue);
            }
        });
    }

    public Long rPush(final String key, final String value, int db) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = serializer.serialize(value);
                return connection.rPush(serKey, serValue);
            }
        });
    }


    public String lPop(final String key, int db) {
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = connection.lPop(serKey);
                if (serValue == null || serValue.length == 0) {
                    return null;
                } else {
                    return serializer.deserialize(serValue);
                }
            }
        });
    }

    public byte[] getObject(final String key, int db) {
        return redisTemplate.execute(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serValue = connection.get(serKey);
                if (serValue == null || serValue.length == 0) {
                    return null;
                }
                return serValue;
            }
        });
    }

    public Long lLen(final String key, int db) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                return connection.lLen(serKey);
            }
        });
    }

    public void addStringTime(final String key, final String field, Long time, int db) {
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serField = serializer.serialize(field);
                connection.setEx(serKey, time, serField);
                return null;
            }
        });
    }

    public Boolean setNXStringTime(final String key, final String field, Long time, int db) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serField = serializer.serialize(field);
                Boolean result = connection.setNX(serKey, serField);
                if (result) {
                    connection.expire(serKey, time);
                }
                return result;
            }
        });
    }

    public void setObject(final String key, final Object field, int db) {
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serField = SerializeUtil.serialize(field);
                connection.set(serKey, serField);
                return null;
            }
        });
    }

    public void setString(final String key, final String value, int db) {
        redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.select(db);
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] serKey = serializer.serialize(key);
                byte[] serField = serializer.serialize(value);
                connection.set(serKey, serField);
                return null;
            }
        });
    }

    public Object getTokeObject(final String key, int db) {
        byte[] object = getObject(key, db);
        if (object != null) {
            return SerializeUtil.unserialize(object);
        }

        return null;
    }


}