package io.aquerr.killorder;

import javax.inject.Singleton;

/**
 * Created by Aquerr on 2018-02-15.
 */

@Singleton
public abstract class PluginPermissions
{
    public static final String HELP_COMMAND = "killorder.help";

    public static final String SELECT_COMMAND = "killorder.create";
    public static final String LIST_COMMAND = "killorder.list";

    public static final String VERSION_NOTIFY = "killorder.version.notify";
}
