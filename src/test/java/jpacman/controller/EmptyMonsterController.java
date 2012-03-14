package jpacman.controller;

/**
 * A monster controller that doesn't move any monsters,
 * allowing us to do the moving ourselves in this test suite.
 */
public class EmptyMonsterController implements IMonsterController
{
    
    /**
     * @see IMonsterController
     */
    public void doTick()
    { /* Do nothing */ }
    
    /**
     * @see IMonsterController
     */
    public void start()
    { /* Do nothing */ }
    
    /**
     * @see IMonsterController
     */
    public void stop()
    { /* Do nothing */ }
}