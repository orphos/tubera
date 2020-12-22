// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera.runtime;

import java.util.HashMap;

public class IdEnv {
    private static class Scopes {
        public final HashMap<String, Identifier> head;
        public final Scopes tail;

        public Scopes() {
            this(null);
        }

        public Scopes(Scopes tail) {
            this.head = new HashMap<>();
            this.tail = tail;
        }
    }

    private final Scopes scopes;

    public IdEnv() {
        this(new Scopes());
    }

    private IdEnv(Scopes scopes) {
        this.scopes = scopes;
    }

    public Identifier find(String name) {
        Scopes scopes = this.scopes;
        while (scopes != null) {
            Identifier ret = scopes.head.get(name);
            if (ret != null)
                return ret;
            scopes = scopes.tail;
        }
        return null;
    }

    public Identifier get(String name) {
        Identifier ret = find(name);
        if (ret == null)
            throw new RuntimeException(String.format("Identifier %s not found", name));
        return ret;
    }

    public Identifier getOrElseUpdate(String name) {
        Identifier ret = find(name);
        if (ret != null)
            return ret;
        ret = new Identifier(name);
        scopes.head.put(name, ret);
        return ret;
    }

    public Identifier put(String name) {
        Identifier id = new Identifier(name);
        scopes.head.put(name, id);
        return id;
    }

    public IdEnv withNewScope() {
        return new IdEnv(new Scopes(scopes));
    }

    public IdEnv outerScope() {
        return new IdEnv(scopes.tail);
    }
}
