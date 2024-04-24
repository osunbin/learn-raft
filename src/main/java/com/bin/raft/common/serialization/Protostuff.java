package com.bin.raft.common.serialization;

import com.bin.raft.core.raftop.RaftOp;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.protostuff.ByteBufInput;
import io.protostuff.ByteBufOutput;
import io.protostuff.Input;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class Protostuff {

    private final ConcurrentHashMap<Class<?>, Schema<?>> schemaMap//
            = new ConcurrentHashMap<>(16);


    private static final Schema<RaftOp> operationSchema
            = RuntimeSchema.getSchema(RaftOp.class);

    public static RaftOp readOperation(ByteBuf byteBuf) throws IOException {
        int totalLen = byteBuf.readIntLE();
        Input input = new ByteBufInput(byteBuf, true);
        RaftOp operation = operationSchema.newMessage();
        operationSchema.mergeFrom(input,operation);
        return operation;
    }


    public static ByteBuf writeOperation(int initSize, RaftOp op) throws IOException {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(initSize);
        buffer.writeIntLE(1);
        ByteBufOutput output = new ByteBufOutput(buffer);
        operationSchema.writeTo(output,op);
        buffer.writeIntLE(buffer.readableBytes() - 4);
        return buffer;
    }



    private <T> Schema<T> schema(Class<T> clazz) {
        Schema<T> schema = (Schema<T>) schemaMap.get(clazz);

        if (schema != null) {
            return schema;
        }

        schema = (Schema<T>) schemaMap.computeIfAbsent(clazz,
                k -> RuntimeSchema.getSchema(clazz));
        return schema;
    }
}
