package br.toe.engine.threads;

import br.toe.engine.platform.logger.*;
import br.toe.game.*;
import br.toe.utils.*;

abstract class ToeThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ToeThread.class);

    protected long delta;
    protected long startTime;
    protected long lastTime;

    protected static final long frameOptimalDuration;

    static {
        final var frameRate = GameSettings.getInteger("renderer.frame.rate");

        if (frameRate > 0)
            frameOptimalDuration = Time.NANOS / frameRate;

        else
            frameOptimalDuration = 0;
    }

    protected abstract void doInitialize();
    private void initialize() {
        CurrentThread.setName(getClass().getSimpleName());
        LOGGER.debug("Initializing");
        doInitialize();
    }

    protected abstract void doUpdate();
    public final void run() {
        try {
            initialize();

            LOGGER.debug("Starting Loop");
            while (GameState.running) {
                startTime = Time.nanos();
                delta = startTime - lastTime;
                lastTime = startTime;

                doUpdate();

                var elapsed = System.nanoTime() - startTime;

                if (frameOptimalDuration > 0)
                    if (elapsed < frameOptimalDuration)
                        CurrentThread.sleep(Time.toMillis(frameOptimalDuration - elapsed));

                    else
                        LOGGER.warn(
                                "loop lag expected %s actual %s",
                                Time.toMillis(frameOptimalDuration),
                                Time.toMillis(elapsed)
                        );
            }

        } catch (Throwable ex) {
            GameState.running = false;
            LOGGER.fatal(ex);

        } finally {
            destroy();

        }
    }

    protected abstract void doDestroy();
    private void destroy() {
        LOGGER.debug("Destroying");
        doDestroy();
    }
}
