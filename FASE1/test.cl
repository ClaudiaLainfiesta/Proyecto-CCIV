class Main inherits IO {
    main() : Object {
        {
            if true then
                out_string()
            fi;

            loop
                out_string()
            pool;

            case 1 of
                x : Int => out_string();
            esac;

            in;

            self.methodName();
        }
    };
};
